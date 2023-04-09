package com.example.campus_bbs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.UserMeta

@Composable
@Preview
fun UserPanelInBlog(
    userMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    timeString: String = "time"
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userMeta.userIconUrl)
                .crossfade(true)
                .build(),
            contentDescription = "test image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .height(65.dp)
                .width(65.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = userMeta.userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = timeString)
        }
    }
}

@Composable
@Preview
fun UserPanelInComment(userMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta()) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userMeta.userIconUrl)
                .crossfade(true)
                .build(),
            contentDescription = "test image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .height(65.dp)
                .width(65.dp)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(65.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = userMeta.userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(imageVector = Icons.Filled.ThumbUp, contentDescription = "like")
                }
            }
        }
    }
}