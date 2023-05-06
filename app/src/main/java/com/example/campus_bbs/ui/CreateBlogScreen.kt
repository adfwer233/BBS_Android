package com.example.campus_bbs.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.ui.components.AddImageGrid
import com.example.campus_bbs.ui.components.OnlineVideoPlayer
import com.example.campus_bbs.ui.model.CommunicationViewModel
import com.example.campus_bbs.ui.model.CreateBlogViewModel
import com.example.campus_bbs.ui.model.RecommendationViewModel
import com.example.campus_bbs.utils.LocationUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.exoplayer2.extractor.TrueHdSampleRechunker

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateBlogScreen(
    modifier: Modifier = Modifier,
    mainAppNavController: NavHostController = rememberNavController(),
) {
    val recommendationViewModel: RecommendationViewModel =
        viewModel(LocalContext.current as ComponentActivity)
    val createBlogViewModel: CreateBlogViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val uiState = createBlogViewModel.uiState.collectAsState()

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(9)
    ) { uri: List<Uri> ->
        createBlogViewModel.addImageUrl(uri.map {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            it.toString()
        })
    }

    val videoPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                createBlogViewModel.updateVideoUri(it.toString())
            }
        }


    val localContext = LocalContext.current

    var location = LocationUtils().getLocation(localContext)

    var addressList = LocationUtils().getGeoFromLocation(localContext, location)

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

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
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // add image button
                Button(onClick = {
                    imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }, shape = RoundedCornerShape(3.dp), modifier = Modifier.weight(1f)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add image")
                        Text(text = "Image")
                    }
                }

                // add video button
                Button(
                    onClick = {
                        videoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.VideoOnly
                            )
                        )
                    },
                    shape = RoundedCornerShape(3.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "video")
                        Text(text = "Video")
                    }
                }

                // add location button
                if (uiState.value.location.isEmpty()) {
                    Button(
                        onClick = {
                            if (locationPermissionState.status.isGranted) {
                                if (addressList.isNotEmpty()) {
                                    createBlogViewModel.updateLocation(addressList[0].featureName)
                                }
                            } else {
                                locationPermissionState.launchPermissionRequest()
                            }
                        },
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "location"
                            )
                            Text(text = "Location")
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            createBlogViewModel.updateLocation("")
                        },
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "location"
                            )
                            Text(text = "Location")
                        }
                    }
                }
            }
        }
    ) { contentPadding ->
        editBlog(modifier = modifier.padding(contentPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
            )
        }

        item {
            if(uistate.value.location.isNotEmpty()) {
                Spacer(modifier = Modifier.height(3.dp))
                Card {
                    Row (modifier = Modifier.padding(5.dp)) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "loc icon")
                        Text(uistate.value.location)
                    }
                }
            }
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
    val uiState by createBlogViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxHeight()
    ) {

        AddImageGrid(imageUrlList = uiState.imageUrlList) {
            createBlogViewModel.removeImageUrl(it)
        }

        if (uiState.videoUrl.isNotEmpty())
            OnlineVideoPlayer(videoUrl = uiState.videoUrl, modifier, showDelete = true) {
                createBlogViewModel.removeVideoUri()
            }
    }
}