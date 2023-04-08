package com.example.campus_bbs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.R
import com.example.campus_bbs.data.FakeDataGenerator

@Composable
fun ImageComponent(
    imageUrl: String,
    modifier: Modifier = Modifier,
    isGrid: Boolean = false
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "test image",
        contentScale = ContentScale.Crop,
        modifier = if (isGrid) modifier.aspectRatio(1F) else modifier
    )
}

@Preview
@Composable
fun ImageGrid(
    imageList: List<String> = FakeDataGenerator().generateImageUrlList(7),
    modifier: Modifier = Modifier
) {
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
                                ImageComponent(imageList[index], isGrid = true)
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