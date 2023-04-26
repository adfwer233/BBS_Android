package com.example.campus_bbs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.UserMeta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MessageReceivedComponent(
    userMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    message: String = "Test String",
    modifier: Modifier = Modifier
) {
    Row() {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userMeta.userIconUrl)
                .crossfade(true)
                .build(),
            contentDescription = "test image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .height(45.dp)
                .width(45.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = userMeta.userName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Thin,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
            Card {
                Text(text = message, Modifier.width(200.dp).padding(5.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MessageSentComponent(
    userMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    message: String = "Test String".repeat(2),
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = " ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Thin,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
            Card(
                modifier = Modifier
            ) {
                Text(text = message, Modifier.width(200.dp).padding(5.dp), textAlign = TextAlign.End)
            }
        }
        Spacer(modifier = Modifier.width(15.dp))
        Box() {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userMeta.userIconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "test image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .height(45.dp)
                    .width(45.dp)
                    .background(Color.Red)
            )
        }

    }
}

