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
    val recommendationViewModel: RecommendationViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val blogViewModel: BlogViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    postId?.let {
        val blog = recommendationViewModel.uiState.collectAsState().value.blogList.find { it.id == postId }!!
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
                                putExtra(Intent.EXTRA_TEXT, "43.138.60.13:8085")
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
                        OutlinedTextField(
                            value = uiState.value.comment,
                            onValueChange = { blogViewModel.updateComment(it) },
                            label = { Text(text = "Comment") },
                        )
                        IconButton(
                            onClick = {
                                 scope.launch {
                                     PostApi.retrofitService.replyPost(
                                         loginViewModel.jwtToken,
                                         blogViewModel.uiState.value.blog.id,
                                         PostReplyDto(uiState.value.comment)
                                     )
                                 }
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "send message"
                            )
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
            Material3RichText() {
                Heading(5, blog.blogTitle)
                Markdown(content = blog.blogContent)
            }
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