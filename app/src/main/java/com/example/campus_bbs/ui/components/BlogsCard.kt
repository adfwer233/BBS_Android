package com.example.campus_bbs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun BlogsCard(
    moreButtonOnClick : () -> Unit = { },
    blog: Blog = FakeDataGenerator().generateSingleFakeBlog(),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = moreButtonOnClick
    ) {

        UserPanelInBlog(userMeta = blog.creator, timeString = blog.createTime.toString())

        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = blog.blogTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = blog.blogContent,
                softWrap = true,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        ImageSingleOrGrid(imageUrlList = blog.imageUrlList)

        Spacer(modifier = Modifier.height(3.dp))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            IconButton(onClick = { /*TODO*/ }, modifier= Modifier.weight(1F)) {
                Row {
                    Icon(imageVector = Icons.Filled.Menu, "test")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = blog.subscribedNumber.toString())
                }
            }
            IconButton(onClick = { /*TODO*/ }, modifier= Modifier.weight(1F)) {
                Row {
                    Icon(imageVector = Icons.Filled.Share, "test")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = blog.blogComments.size.toString())
                }
            }
            IconButton(onClick = { /*TODO*/ }, modifier= Modifier.weight(1F)) {
                Row {
                    Icon(imageVector = Icons.Filled.ThumbUp, "test")
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = blog.likedNumber.toString())
                }
            }
        }
    }
}