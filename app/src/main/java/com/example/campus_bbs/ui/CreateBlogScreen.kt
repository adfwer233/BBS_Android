package com.example.campus_bbs.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.ui.components.AddImageGrid
import com.example.campus_bbs.ui.components.OnlineVideoPlayer
import com.example.campus_bbs.ui.model.CommunicationViewModel
import com.example.campus_bbs.ui.model.CreateBlogViewModel
import com.example.campus_bbs.ui.model.RecommendationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBlogScreen(
    modifier: Modifier = Modifier,
    mainAppNavController: NavHostController = rememberNavController(),
) {
    val recommendationViewModel: RecommendationViewModel = viewModel(LocalContext.current as ComponentActivity)
    val createBlogViewModel: CreateBlogViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Create New Blog") },
                navigationIcon = {
                    Button(onClick = { mainAppNavController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = {
                            val blog = createBlogViewModel.generateBlogFromState()
                            recommendationViewModel.pushFront(blog)
                            createBlogViewModel.clearUiState()
                            mainAppNavController.navigateUp()
                        }) {
                            Row {
                                Icon(imageVector = Icons.Filled.Send, contentDescription = "test")
                            }
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        editBlog(modifier = modifier.padding(contentPadding))
    }
}

@Composable
fun editBlog(
    modifier: Modifier = Modifier,
    createBlogViewModel: CreateBlogViewModel = viewModel(LocalContext.current as ComponentActivity),
) {

    val uistate = createBlogViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),

    ) {
        item {
            TextField(
                label = { Text(text = "Title") },
                value = uistate.value.titleText,
                onValueChange = {
                    createBlogViewModel.updateTitleText(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            TextField(
                label = { Text(text = "Content") },
                value = uistate.value.contentText,
                onValueChange = {
                    createBlogViewModel.updateContentText(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        item {
            MultiMediaPanel()
        }
    }
}

@Composable
fun MultiMediaPanel(
    modifier: Modifier = Modifier,
    createBlogViewModel: CreateBlogViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    var tabState by remember {
        mutableStateOf(0)
    }

    val uiState by createBlogViewModel.uiState.collectAsState()

    val titles = listOf("Image", "Video")
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia(9)) { uri: List<Uri> ->
        createBlogViewModel.updateImageUrl(uri.map {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            it.toString()
        })
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
        createBlogViewModel.updateVideoUri(it.toString())
    }

    Column() {
        TabRow(selectedTabIndex = tabState) {
            titles.forEachIndexed { index, title ->
                Tab(selected = tabState == index, onClick = {tabState = index}) {
                    Text(text = title)
                }
            }
        }

        if (titles[tabState] == "Image") {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Images", Modifier.padding(10.dp))
                    IconButton(onClick = { imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "add image")
                    }
                }

                AddImageGrid(imageUrlList = uiState.imageUrlList) {
                    createBlogViewModel.removeImageUrl(it)
                }
            }
        }
        
        if (titles[tabState] == "Video") {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Video", Modifier.padding(10.dp))
                    IconButton(onClick = { videoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)) }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "add video")
                    }
                }

                OnlineVideoPlayer(videoUrl = uiState.videoUrl, modifier)
            }
        }
    }
}