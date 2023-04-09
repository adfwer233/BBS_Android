package com.example.campus_bbs.ui.components

import android.text.BoringLayout
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.campus_bbs.data.FakeDataGenerator

@Composable
fun ImageComponent(
    imageUrl: String,
    modifier: Modifier = Modifier,
    isGrid: Boolean = false,
    showDelete: Boolean = false,
    deleteOnClick: () -> Unit = {},
    imageOnClick: () -> Unit = {}
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var commonModifier = modifier

    if (isGrid) {
        commonModifier = modifier.clickable {
            imageOnClick()
        }
    } else {
        commonModifier = modifier.clickable {
            showDialog = true
        }
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = !showDialog },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            FullScreenImageRoller(
                imageUrlList = listOf(imageUrl),
                initState = 0,
                fullScreenImageOnClick = { showDialog = false }
            )
        }
    }

    Box(modifier = Modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "test image",
            contentScale = ContentScale.Crop,
            modifier = if (isGrid) commonModifier.aspectRatio(1F) else commonModifier,
        )
        if (showDelete) {
            IconButton(onClick = deleteOnClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.LightGray, shape = CircleShape)
                    .width(20.dp)
                    .height(20.dp)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "test")
            }
        }
    }
}

@Preview
@Composable
fun ImageGrid(
    imageList: List<String> = FakeDataGenerator().generateImageUrlList(7),
    modifier: Modifier = Modifier,
    withDelete: Boolean = false,
    deleteOnClick: (Int) -> Unit = {}
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    var indexOfClicked by remember {
        mutableStateOf(0)
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = !showDialog },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
        ) {
            FullScreenImageRoller(
                imageUrlList = imageList,
                initState = indexOfClicked,
                fullScreenImageOnClick = { showDialog = false }
            )
        }
    }

    var placeholderIndex = imageList.size + (3 - imageList.size % 3)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        for (rowIndex in (0..(imageList.size - 1)/3)) {
            Box {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    for (columnIndex in (0..2)) {
                        val index = 3 * rowIndex + columnIndex
                        if (index < imageList.size) {
                            Box(modifier = Modifier.weight(1F)) {
                                ImageComponent(
                                    imageList[index],
                                    isGrid = true,
                                    showDelete = withDelete,
                                    deleteOnClick = { deleteOnClick(index)},
                                    imageOnClick = {
                                        indexOfClicked = index
                                        showDialog = true
                                    }
                                )
                            }
                        } else if (index < placeholderIndex) {
                            Box(modifier = Modifier.weight(1F))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddImageGrid(
    imageUrlList: List<String>,
    modifier: Modifier = Modifier,
    deleteOnClick: (Int) -> Unit = {}
) {
    ImageGrid(
        imageUrlList,
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp)),
        withDelete = true,
        deleteOnClick = deleteOnClick
    )
}

@Composable
fun ImageSingleOrGrid(
    imageUrlList: List<String>,
    modifier: Modifier = Modifier
) {
    if (imageUrlList.size > 1) {
        ImageGrid(
            imageUrlList, modifier = Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(5.dp))
        )
    } else if (imageUrlList.size == 1) {
        ImageComponent(imageUrl = imageUrlList[0], modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(5.dp))
        )
    }
}