package com.example.campus_bbs.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.R
import com.example.campus_bbs.ui.components.CommentSheet
import com.example.campus_bbs.ui.components.FullScreenImageRoller
import com.example.campus_bbs.ui.model.UserViewModel
import kotlinx.coroutines.launch
import java.util.function.IntConsumer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
@Preview
fun EditProfileScreen() {

    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val userState = userViewModel.currentUserState.collectAsState()

    var selection = remember {
        mutableStateOf("")
    }

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
//        skipHalfExpanded = true
    )

    val scope = rememberCoroutineScope()


    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let {
            userViewModel.updateUserImageUri(it.toString())
        }
    }
    ModalBottomSheetLayout(
        sheetContent = {
            when(selection.value) {
                "name" -> EditName()
                "profile" -> EditProfile()
            }
        },
        sheetState = sheetState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Edit Profile") },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                        }
                    },
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userState.value.userIconUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "test image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(105.dp)
                            .width(105.dp)
                            .clickable {
                                imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider()


                ListItem(
                    trailing = {
                        Row {
                            Text(text = userState.value.userName)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "trail"
                            )
                        }
                    },
                    modifier = Modifier.clickable {
                        selection.value = "name"
                        scope.launch { sheetState.show() }
                    }
                ) {
                    Text(text = "Name")
                }

                Divider()

                ListItem(
                    trailing = {
                        Row {
                            Text(text = userState.value.userName)
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "trail"
                            )
                        }
                    },
                    modifier = Modifier.clickable {
                        selection.value = "profile"
                        scope.launch { sheetState.show() }
                    }
                ) {
                    Text(text = "Profile")
                }

                Divider()

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditName() {
    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val userState = userViewModel.currentUserState.collectAsState()

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "Edit Name")}) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column (modifier = Modifier.padding(10.dp)) {
                OutlinedTextField(value = userState.value.userName, onValueChange = { }, modifier = Modifier.fillMaxWidth())

                OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Save")
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile() {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(text = "Edit Profile")}) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            Column (modifier = Modifier.padding(10.dp)) {
                OutlinedTextField(value = "test name", onValueChange = { }, modifier = Modifier.fillMaxWidth())

                OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Save")
                }
            }
        }

    }
}