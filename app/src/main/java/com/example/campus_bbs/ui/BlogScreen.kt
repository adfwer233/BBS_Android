package com.example.campus_bbs.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.components.*
import com.example.campus_bbs.ui.model.BlogViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BlogScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    var commentToShow by remember {
        mutableStateOf(FakeDataGenerator().generateSingleComment(2))
    }

    ModalBottomSheetLayout(
        sheetContent = {
            CommentSheet(comment = commentToShow)
        },
        sheetState = sheetState
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Blog Detail") },
                    navigationIcon = {
//                        Button(onClick = { mainAppNavController.navigateUp() }) {
//                            Icon(
//                                imageVector = Icons.Filled.ArrowBack,
//                                contentDescription = "Go back"
//                            )
//                        }
                        IconButton(onClick = { mainAppNavController.navigateUp() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "exit")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "share")
                        }
                    }
                )
            }
        ) { contentPadding ->
            BlogScreenMain(
                modifier = modifier.padding(contentPadding),
//                blogViewModel = blogViewModel,
                showComment = {
                    commentToShow = it
                    scope.launch { sheetState.show() }
                }
            )
        }
    }
}

@Composable
fun BlogScreenMain(
    modifier: Modifier = Modifier,
    blogViewModel: BlogViewModel = viewModel(LocalContext.current as ComponentActivity),
    showComment: (BlogComment) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        item {
            BlogMainCard(blog = blogViewModel.getBlog())
        }

        items(blogViewModel.uiState.value.blog.blogComments) {
            BlogCommentCard(
                comment = it,
                moreCommentOnClick = { showComment(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BlogMainCard(
    modifier: Modifier = Modifier,
    blog: Blog = FakeDataGenerator().generateSingleFakeBlog()
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        UserPanelInBlog(userMeta = blog.creator, timeString = blog.createTime.toString())

        Column(modifier = Modifier.padding(5.dp)) {
            Text(text = blog.blogTitle, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = blog.blogContent,
            )
        }

        ImageSingleOrGrid(imageUrlList = blog.imageUrlList)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BlogCommentCard(
    modifier: Modifier = Modifier,
    comment: BlogComment = FakeDataGenerator().generateSingleComment(2),
    moreCommentOnClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        UserPanelInComment(userMeta = comment.creator)

        Spacer(modifier = Modifier.height(3.dp))

        Box(modifier = Modifier.padding(5.dp)) {
            Text(text = comment.commentContent)
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

                if (comment.followingComment.size > 3) {
                    Button(
                        onClick = moreCommentOnClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
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