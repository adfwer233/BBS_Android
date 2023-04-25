package com.example.campus_bbs.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ImagePicker(
    modifier: Modifier = Modifier
) {
    var imageUriList by remember {
        mutableStateOf<List<Uri>?>(null)
    }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia(5)) {
        uri: List<Uri> ->
        imageUriList = uri
    }

    Column() {
        Button(onClick = {
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            Text(text = "Pick image")
        }

        if (imageUriList != null) {
            LazyColumn() {
                items(imageUriList!!) {
                    ImageComponent(imageUrl = it.toString())
                }
            }
        }
    }


}