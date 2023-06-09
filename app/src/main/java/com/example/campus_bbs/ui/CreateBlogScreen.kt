package com.example.campus_bbs.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.Global
import com.example.campus_bbs.ui.components.AddImageGrid
import com.example.campus_bbs.ui.components.OnlineVideoPlayer
import com.example.campus_bbs.ui.model.CreateBlogViewModel
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.model.RecommendationViewModel
import com.example.campus_bbs.ui.network.*
import com.example.campus_bbs.utils.LocationUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val uiState = createBlogViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    var imageUris : MutableList<MultipartBody.Part> = mutableListOf()
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
        imageUris.addAll(uri.map {
            val inputStream = context.contentResolver.openInputStream(it)
            val imageBitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
            val stream = ByteArrayOutputStream()
            imageBitmap.asAndroidBitmap()
                .compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val requestBody = stream.toByteArray()
                .toRequestBody(
                    "image/*".toMediaType()
                )
            MultipartBody.Part.createFormData(
                "image",
                "image1.jpeg",
                requestBody
            )
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
                scope.launch {
                    val inputStream = context.contentResolver.openInputStream(it)
                    var fileOutputStream: FileOutputStream? = null

                    var videoName: String = ""
                    val cursor = context.contentResolver.query(it, null, null, null, null)
                    cursor?.let { cur ->
                        if (cursor.moveToFirst()) {
                            videoName =
                                cur.getString(cur.getColumnIndexOrThrow("_display_name"))
                            cursor.close()
                        }
                    }
                    val videoDir = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
                        "Videos"
                    )
                    if (!videoDir.exists()) {
                        videoDir.mkdirs()
                    }
                    val videoFile = File(videoDir, videoName)

                    fileOutputStream = FileOutputStream(videoFile)
                    inputStream?.copyTo(fileOutputStream)
                    inputStream?.close()

                    val bodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                    bodyBuilder.addFormDataPart(
                        "file",
                        videoFile.name,
                        videoFile.asRequestBody("video/*".toMediaTypeOrNull())
                    )

                    //                val requestBody = videoFile.asRequestBody("video/".toMediaTypeOrNull())

                    val requestBody = bodyBuilder.build()
                    val requestBodyWithProgress = ReqBodyWithProgress(requestBody) { progress ->
                        //If you use logging interceptor this will trigger twice
                        Log.d("Upload Progress:", "" + progress)
                        if (progress - uiState.value.progress > 0.15)
                            createBlogViewModel.updateProgress(progress)
                    }

                    createBlogViewModel.updateProgressVisible(true)
                    createBlogViewModel.updateProgress(0f)
                    val resp = VideoApi.retrofitService.upload(
                        loginViewModel.jwtToken,
                        requestBodyWithProgress
                    )

                    createBlogViewModel.updateVideoUri(Global.BASE_HTTP_URL + resp.headers()["location"])
                    println(createBlogViewModel.uiState.value.videoUrl)
                    createBlogViewModel.updateProgressVisible(false)
                }

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
                            scope.launch {
                                Log.e("create", imageUris.toString())
                                try {
                                    PostApi.retrofitService.createPost(
                                        loginViewModel.jwtToken,
                                        blog.blogTitle.toRequestBody("text/plain".toMediaType()),
                                        blog.blogContent.toRequestBody("text/plain".toMediaType()),
                                        blog.location.toRequestBody("text/plain".toMediaType()),
                                        blog.tag.joinToString().toRequestBody("text/plain".toMediaType()),
                                        imageUris,
                                        blog.videoUrl.toRequestBody("text/plain".toMediaType())
                                    )
                                    recommendationViewModel.updateBlogList(loginViewModel.jwtToken)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Log.e("Create Post", e.toString())
                                    Toast.makeText(
                                        context, "网络错误",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
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
                                location = LocationUtils().getLocation(localContext)
                                addressList =
                                    LocationUtils().getGeoFromLocation(localContext, location)
                                if (addressList.isNotEmpty() && addressList[0].featureName != null) {
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
                            if (locationPermissionState.status.isGranted) {
                                Text(text = "Location")
                            } else {
                                Text(text = "Permission")
                            }
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

@Composable
fun progressBar() {
    val createBlogViewModel: CreateBlogViewModel = viewModel(LocalContext.current as ComponentActivity)
    val progressState = createBlogViewModel.progressState.collectAsState()
    val animatedProgress by animateFloatAsState(
        targetValue = progressState.value,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth()
            .semantics(mergeDescendants = true) {}
            .padding(10.dp),
        progress = animatedProgress,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun editBlog(
    modifier: Modifier = Modifier,
    createBlogViewModel: CreateBlogViewModel = viewModel(LocalContext.current as ComponentActivity),
) {

    val uistate = createBlogViewModel.uiState.collectAsState()

    var openTagSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
//        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()




    ModalBottomSheetLayout(
        sheetContent = {
            addTagSheet()
        },
        sheetState = sheetState,
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    if (uistate.value.progressBarVisible) {
                        progressBar()
                    }
                }
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
                    if (uistate.value.location.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Card {
                            Row(modifier = Modifier.padding(5.dp)) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "loc icon"
                                )
                                Text(uistate.value.location)
                            }
                        }
                    }
                }

                item {
                    MultiMediaPanel()
                }
            }
            // Tags
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    items(uistate.value.tagList) {
                        Card {
                            Text(text = ("# $it"), Modifier.padding(5.dp))
                        }
                    }
                }
                OutlinedIconButton(
                    onClick = { scope.launch { sheetState.show() } }, modifier = Modifier
                        .align(Alignment.TopEnd)
                        .width(30.dp)
                        .height(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "add tag",
                    )
                }
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addTagSheet() {
    val createBlogViewModel: CreateBlogViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val uiState = createBlogViewModel.uiState.collectAsState()

    Column() {

        Text("Add New Tag:")
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(value = uiState.value.tagInput
                , onValueChange = {
                    createBlogViewModel.updateTagInput(it)
                })

            OutlinedIconButton(
                onClick = {
                    createBlogViewModel.insertTag(createBlogViewModel.uiState.value.tagInput)
                    createBlogViewModel.clearTag()
                }, modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add tag",
                )
            }
        }

        Spacer(Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            items(uiState.value.tagList) {
                Card {
                    Text(text = ("# $it"), Modifier.padding(5.dp))
                }
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}