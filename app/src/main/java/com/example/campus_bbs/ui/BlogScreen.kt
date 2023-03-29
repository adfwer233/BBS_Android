package com.example.campus_bbs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.model.BlogViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    blogViewModel: BlogViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Blog Detail") },
                navigationIcon = {
                    Button(onClick = { mainAppNavController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ) { contentPadding ->
        BlogScreenMain(
            modifier = modifier.padding(contentPadding),
            blogViewModel = blogViewModel
        )
    }
}

@Composable
fun BlogScreenMain(
    modifier: Modifier = Modifier,
    blogViewModel: BlogViewModel = viewModel()
) {
    LazyColumn(
        modifier = modifier
    ) {

        item {
            BlogMainCard(blog = blogViewModel.uiState.value.blog)
        }

        items(blogViewModel.uiState.value.blog.blogComments) {
            BlogCommentCard(
                comment = it
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BlogMainCard(
    modifier: Modifier = Modifier,
    blog: Blog = FakeDataGenerator().generateSingleFakeBlog(),
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Icon(Icons.Filled.Face, contentDescription = "user")
            Column {
                Text(text = blog.creatorName)
                Text(text = blog.createTime.toString())
            }
        }
        Text(text = blog.blogTitle, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = blog.blogContent)
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = { }
        ) {
            Text(text = "Text Button")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BlogCommentCard(
    modifier: Modifier = Modifier,
    comment: BlogComment = FakeDataGenerator().generateSingleComment(2)
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Icon(Icons.Filled.Face, contentDescription = "user")
            Column {
                Text(text = comment.creator.userName)
                Text(text = comment.createTime.toString())
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = comment.commentContent)
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = { }
        ) {
            Text(text = "Text Button")
        }

        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(25.dp))
            Column(
                modifier = modifier
            ) {
                repeat(3) {index ->
                    if (index < comment.followingComment.size) {
                        FollowingComment(comment = comment.followingComment[index])
                    }
                }

                if (3 >= comment.followingComment.size) {
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "More comment")
                    }
                }
            }
        }
    }
}

@Composable
fun FollowingComment(comment: BlogComment, modifier: Modifier = Modifier) {
    Row( modifier = modifier ) {
        Text(text = comment.creator.userName, color = Color.Blue)
        Spacer(modifier = modifier.width(3.dp))
        Text(text = comment.commentContent)
    }
}