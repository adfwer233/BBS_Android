package com.example.campus_bbs.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.UserMeta

@Composable
fun UserPanelInBlog(userMeta: UserMeta, timeString: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
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
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = userMeta.userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = timeString)
        }
    }
}