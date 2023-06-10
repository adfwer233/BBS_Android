package com.example.campus_bbs.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.AppViewModelProvider
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.network.PostApi
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.Heading
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.Material3RichText
import com.halilibo.richtext.ui.textOrderedMarkers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Integer.min

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
        val scope = rememberCoroutineScope()
        UserPanelInBlog(userMeta = blog.creator, timeString = blog.createTime.toString())
        val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
        Column(modifier = Modifier.padding(5.dp)) {
            Material3RichText(
                style = RichTextStyle(),
                modifier = Modifier
            ) {
                Heading(4, blog.blogTitle)
                Markdown(content = blog.blogContent.substring(0, min(300, blog.blogContent.length)))
            }
            if (blog.blogContent.length > 300) {
                Text(text = "...", textAlign = TextAlign.End)
            }
        }
        
        ImageSingleOrGrid(imageUrlList = blog.imageUrlList)

        if(blog.location.isNotEmpty()) {
            Spacer(modifier = Modifier.height(3.dp))
            Card {
                Row (modifier = Modifier.padding(5.dp)) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = "loc icon")
                    Text(blog.location)
                }
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            IconButton(onClick = {
                scope.launch {
                    if(blog.liked){
                        PostApi.retrofitService.unlikePost(loginViewModel.jwtToken, blog.id)
                    } else {
                        PostApi.retrofitService.likePost(loginViewModel.jwtToken, blog.id)
                    }

                }
            }, modifier= Modifier.weight(1F)) {
                Row {
                    if (blog.liked) {
                        Icon(imageVector = Icons.Default.Menu, "", )
                    } else {
                        Icon(imageVector = Icons.Filled.Menu, "test")
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = blog.blogComments.size.toString())
                }
            }
            IconButton(onClick = {
                scope.launch {
                    if(blog.subscribed){
                        PostApi.retrofitService.uncollectPost(loginViewModel.jwtToken, blog.id)
                    } else {
                        PostApi.retrofitService.collectPost(loginViewModel.jwtToken, blog.id)
                    }
                }
            }, modifier= Modifier.weight(1F)) {
                Row {
                    if (blog.subscribed) {
                        Icon(imageVector = Icons.Filled.Star, "test", tint = Color.Yellow)
                    } else {
                        Icon(imageVector = Icons.Filled.Star, "test")
                    }

                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = blog.subscribedNumber.toString())
                }
            }
            IconButton(onClick = {
                scope.launch {
                    if(blog.liked){
                        PostApi.retrofitService.unlikePost(loginViewModel.jwtToken, blog.id)
                    } else {
                        PostApi.retrofitService.likePost(loginViewModel.jwtToken, blog.id)
                    }
                }
            }, modifier= Modifier.weight(1F)) {
                Row {
                    if (blog.liked) {
                        Icon(imageVector = Icons.Filled.ThumbUp, "test", tint = Color.Red  )
                    } else {
                        Icon(imageVector = Icons.Filled.ThumbUp, "test")
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = blog.likedNumber.toString())
                }
            }
        }
    }
}