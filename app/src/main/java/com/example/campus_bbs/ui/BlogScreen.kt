package com.example.campus_bbs.ui

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.components.*
import com.example.campus_bbs.ui.model.BlogViewModel
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.model.RecommendationViewModel
import com.example.campus_bbs.ui.network.PostApi
import com.example.campus_bbs.ui.network.PostReplyDto
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.Heading
import com.halilibo.richtext.ui.material3.Material3RichText
import kotlinx.coroutines.launch
import java.util.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BlogScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    postId: String? = "",
) {

    // select the blog by id
    val recommendationViewModel: RecommendationViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val blogViewModel: BlogViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val loginViewModel: LoginViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    postId?.let {
        val blog =
            recommendationViewModel.uiState.collectAsState().value.blogList.find { it.id == postId }!!
        blogViewModel.updateBlog(blog)
    }

    var comment: String = ""

    val localContent = LocalContext.current
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()

    var commentToShow by remember {
        mutableStateOf(FakeDataGenerator().generateSingleComment(2))
    }

    val uiState = blogViewModel.uiState.collectAsState()

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
                        IconButton(onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TITLE, "Campus BBS")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Post in Campus BBS: \n Title: ${uiState.value.blog.blogTitle} \n Creator: ${uiState.value.blog.creator.userName} \n Content: ${uiState.value.blog.blogContent} \n Open this link in browser to view post in Campus BBS 43.138.60.13:8085/redirect/${uiState.value.blog.id}"
                                )
                                type = "text/html"
                            }

                            val shareIntent = Intent.createChooser(sendIntent, null)
                            startActivity(localContent, shareIntent, null)
                        }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = "share")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    content = {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = uiState.value.comment,
                                onValueChange = { blogViewModel.updateComment(it) },
                                label = { Text(text = "Comment") },
                                modifier = Modifier.weight(10f)
                            )
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        PostApi.retrofitService.replyPost(
                                            loginViewModel.jwtToken,
                                            blogViewModel.uiState.value.blog.id,
                                            PostReplyDto(uiState.value.comment)
                                        )
                                        recommendationViewModel.updateBlogList(loginViewModel.jwtToken)
                                    }
                                    blogViewModel.updateComment("")
                                },
                                modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "send message"
                                )
                            }
                        }
                    },
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

        items(blogViewModel.uiState.value.blog.blogComments.sortedBy { comment -> comment.createTime }) {
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
    var recommendationViewModel: RecommendationViewModel =
        viewModel(LocalContext.current as ComponentActivity)
    val loginViewModel: LoginViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        UserPanelInBlog(userMeta = blog.creator, timeString = blog.createTime.toString())

        Column(modifier = Modifier.padding(5.dp)) {
            Material3RichText() {
                Heading(5, blog.blogTitle)
                Markdown(content = blog.blogContent)
            }
        }

        ImageSingleOrGrid(imageUrlList = blog.imageUrlList)

        if (blog.videoUrl.isNotEmpty()) {
            OnlineVideoPlayer(videoUrl = blog.videoUrl, modifier, showDelete = false)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            IconButton(onClick = {
                scope.launch {
                    if (blog.subscribed) {
                        PostApi.retrofitService.uncollectPost(loginViewModel.jwtToken, blog.id)
                    } else {
                        PostApi.retrofitService.collectPost(loginViewModel.jwtToken, blog.id)
                    }
                    recommendationViewModel.updateBlogList(loginViewModel.jwtToken)
                }
            }, modifier = Modifier.weight(1F)) {
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
                    if (blog.liked) {
                        PostApi.retrofitService.unlikePost(loginViewModel.jwtToken, blog.id)
                    } else {
                        PostApi.retrofitService.likePost(loginViewModel.jwtToken, blog.id)
                    }
                    recommendationViewModel.updateBlogList(loginViewModel.jwtToken)
                }
            }, modifier = Modifier.weight(1F)) {
                Row {
                    if (blog.liked) {
                        Icon(imageVector = Icons.Filled.ThumbUp, "test", tint = Color.Red)
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

        UserPanelInComment(userMeta = comment.creator, createTime = comment.createTime.toString())

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
            }
        }
    }
}

@Composable
fun FollowingComment(comment: BlogComment, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = comment.creator.userName, color = Color.Blue)
        Spacer(modifier = modifier.width(3.dp))
        Text(text = comment.commentContent)
    }
}