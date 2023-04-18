package com.example.campus_bbs.ui

import android.provider.MediaStore.Images
import android.util.Log
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.components.AddImageGrid
import com.example.campus_bbs.ui.components.ImageSingleOrGrid
import com.example.campus_bbs.ui.components.OnlineVideoPlayer
import com.example.campus_bbs.ui.model.CreateBlogViewModel
import com.example.campus_bbs.ui.model.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBlogScreen(
    modifier: Modifier = Modifier,
    mainAppNavController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel
) {
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
                        IconButton(onClick = { mainViewModel.createBlogViewModel.saveState() }) {
                            Icon(imageVector = Icons.Filled.Build, contentDescription = "")
                        }
                        
                        IconButton(onClick = {
                            val blog = mainViewModel.createBlogViewModel.generateBlogFromState()
                            mainViewModel.recommendationViewModel.pushFront(blog)
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
        editBlog(modifier = modifier.padding(contentPadding), createBlogViewModel=mainViewModel.createBlogViewModel)
    }
}

@Composable
fun editBlog(
    modifier: Modifier = Modifier,
    createBlogViewModel: CreateBlogViewModel = viewModel()
) {
    var titleText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(createBlogViewModel.uiState.value.savedTitleText))
    }

    var contentText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(createBlogViewModel.uiState.value.savedContentText))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),

    ) {
        item {
            TextField(
                label = { Text(text = "Title") },
                value = titleText,
                onValueChange = {
                    titleText = it
                    createBlogViewModel.updateTitleText(it.text)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            TextField(
                label = { Text(text = "Content") },
                value = contentText,
                onValueChange = {
                    contentText = it
                    createBlogViewModel.updateContentText(it.text)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        item {
            MultiMediaPanel(createBlogViewModel)
        }
    }
}

@Composable
fun MultiMediaPanel(
    createBlogViewModel: CreateBlogViewModel,
    modifier: Modifier = Modifier
) {
    var tabState by remember {
        mutableStateOf(0)
    }

    val imageUrlList by createBlogViewModel.imageUrlList.collectAsState()

    val titles = listOf("Image", "Video")

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
                    IconButton(onClick = { createBlogViewModel.addRandomImageUrl() }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "add image")
                    }
                }

                AddImageGrid(imageUrlList = imageUrlList) {
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
                    IconButton(onClick = { createBlogViewModel.addRandomImageUrl() }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "add image")
                    }
                }

                OnlineVideoPlayer(videoUrl = "https://cloud.tsinghua.edu.cn/f/d059ce302d864d7ab9ee/?dl=1", modifier)
            }
        }
    }
}