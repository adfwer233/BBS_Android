package com.example.campus_bbs.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.FakeDataGenerator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullScreenImageRoller(
    imageUrlList: List<String>,
    initState: Int,
    modifier: Modifier = Modifier
) {

    val pageCount = imageUrlList.size
    val pagerState = rememberPagerState(initState)
    Box {
        HorizontalPager(
            pageCount = pageCount,
            state = pagerState
        ) { page ->
            // Our page content
//            Text(
//                text = "Page: $page " + imageUrlList[page].toString(),
//                modifier = Modifier
//                    .fillMaxSize()
//            )
            
            FullScreenImage(imageUrl = imageUrlList[page])
        }
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
fun FullScreenImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "test image",
            contentScale = ContentScale.Fit,
            modifier = modifier.fillMaxSize()
        )
    }
}