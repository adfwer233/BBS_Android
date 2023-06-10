package com.example.campus_bbs.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.model.NavControlViewModel
import com.example.campus_bbs.ui.model.SearchViewModel
import com.example.campus_bbs.ui.network.PostApi
import com.example.campus_bbs.ui.network.UserApi
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.Collectors

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val searchViewModel: SearchViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val loginViewModel: LoginViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val uiState = searchViewModel.uiState.collectAsState()

    val titles = listOf("Post", "User")

    val pagerState = rememberPagerState(0)
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxHeight()
    ) {

        Row(
            modifier = Modifier.padding(15.dp)
        ) {
            OutlinedTextField(value = uiState.value.input, onValueChange = {searchViewModel.updateInput(it)}, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done))
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedIconButton(onClick = {
                scope.launch {
                    val resp = PostApi.retrofitService.search(
                        loginViewModel.jwtToken,
                        uiState.value.input
                    )
                    searchViewModel.updateResultList(resp)

                    val userResp = UserApi.retrofitService.searchUser(
                        loginViewModel.jwtToken,
                        uiState.value.input
                    )
                    println(userResp)
                    searchViewModel.updateUserResultList(userResp)
                }
            }, modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(45.dp)
                .height(45.dp)) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index, 0F)
                                }
                            },
                            modifier = Modifier
                        ) {
                            Text(text = title, modifier = Modifier.padding(10.dp))
                        }
                    }
                }
            }
        }

        HorizontalPager(
            pageCount = titles.size,
            state = pagerState
        ) { page ->
            when(page) {
                0 -> searchPostTab()
                1 -> searchUserTab()
            }
        }
    }
}

@Composable
fun searchPostTab() {
    val searchViewModel: SearchViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val uiState = searchViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(uiState.value.result.postList) {post ->
                BlogsCard(blog =
                Blog(
                    post.id,
                    post.creator,
                    Date(post.createTime),
                    post.title,
                    post.content,
                    post.images,
                    post.videoUrl,
                    post.tags,
                    post.likesNumber,
                    post.collectedNumber,
                    post.comments.stream().map{ reply -> BlogComment(
                        reply.creator,
                        reply.content,
                        Date(reply.createTime),
                    )
                    }.collect(Collectors.toList()),
                    post.liked,
                    post.collected,
                    post.location
                )
                )
            }
        }
    }
}

@Composable
fun searchUserTab() {
    val searchViewModel: SearchViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val navControlViewModel: NavControlViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val scope = rememberCoroutineScope()

    val uiState = searchViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(uiState.value.userResult.users) {user ->
                Card {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.avatarUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "test image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(65.dp)
                                .width(65.dp)
                                .clickable {
                                    scope.launch {
                                        val resp = UserApi.retrofitService.getUserById(
                                            loginViewModel.jwtToken,
                                            user.userId
                                        )
                                        navControlViewModel.userHome = resp.getUser()
                                        navControlViewModel.mainNavController.navigate("UserHome")
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Column(
                            modifier = Modifier
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .height(65.dp)
                                    .fillMaxWidth()
                            ) {
                                Column() {
                                    Text(
                                        text = user.username,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(text = user.description)
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                    }
                }
            }
        }
    }
}