package com.example.campus_bbs.ui

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.*
import com.example.campus_bbs.utils.LocationUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
fun RecommendationScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
//    val blogList = remember{ mutableStateOf<List<Blog>>(FakeDataGenerator().generateFakeBlogs(10)) }

    val titles = listOf("Timeline", "Hot", "Subscribe")

    var pagerState = rememberPagerState(0)

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
    ) {

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
                                coroutineScope.launch {
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
        ) {

            page ->
            when(page) {
                0 -> DefaultRecommendation(mainAppNavController, Modifier)
                1 -> HotRecommendation(mainAppNavController, Modifier)
                2 -> SubscribedRecommendation(mainAppNavController, Modifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable

fun HotRecommendation(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    var hotViewModel: HotViewModel = viewModel(LocalContext.current as ComponentActivity)
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val hotUiState by hotViewModel.uiState.collectAsState()

    if (hotUiState.blogList.isEmpty()) {
        hotViewModel.updateBlogList(loginViewModel.jwtToken, sort = "hot")
    }

    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    fun refresh () = refreshScope.launch {
        refreshing = true
        delay(1500)
        hotViewModel.updateBlogList(loginViewModel.jwtToken, sort = "hot")
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(modifier = modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(hotViewModel.uiState.value.blogList) {
                BlogsCard({
//                    blogViewModel.updateBlog(it)
                    println("BlogScreen/?id=${it.id}")
                    mainAppNavController.navigate("BlogScreen/?id=${it.id}")
                },
                    it
                )
            }
        }
        PullRefreshIndicator(refreshing = refreshing, state = state, modifier = Modifier.align(Alignment.TopCenter))
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubscribedRecommendation(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    var subscribedViewModel: SubscribedViewModel = viewModel(LocalContext.current as ComponentActivity)
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val subscribedUiState by subscribedViewModel.uiState.collectAsState()

    if (subscribedUiState.blogList.isEmpty()) {
        subscribedViewModel.updateBlogList(loginViewModel.jwtToken)
    }

    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    fun refresh () = refreshScope.launch {
        refreshing = true
        delay(1500)
        subscribedViewModel.updateBlogList(loginViewModel.jwtToken)
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(modifier = modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(subscribedViewModel.uiState.value.blogList) {
                BlogsCard({
//                    blogViewModel.updateBlog(it)
                    println("BlogScreen/?id=${it.id}")
                    mainAppNavController.navigate("BlogScreen/?id=${it.id}")
                },
                    it
                )
            }
        }
        PullRefreshIndicator(refreshing = refreshing, state = state, modifier = Modifier.align(Alignment.TopCenter))
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
fun DefaultRecommendation(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var recommendationViewModel: RecommendationViewModel = viewModel(LocalContext.current as ComponentActivity)
    var blogViewModel: BlogViewModel = viewModel(LocalContext.current as ComponentActivity)
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val recommendationUiState by recommendationViewModel.uiState.collectAsState()

    if (recommendationUiState.blogList.isEmpty()) {
        recommendationViewModel.updateBlogList(loginViewModel.jwtToken)
    }

    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    fun refresh () = refreshScope.launch {
        refreshing = true
        delay(1500)
        recommendationViewModel.updateBlogList(loginViewModel.jwtToken)
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(modifier = modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(recommendationViewModel.uiState.value.blogList) {
                BlogsCard({
//                    blogViewModel.updateBlog(it)
                    println("BlogScreen/?id=${it.id}")
                    mainAppNavController.navigate("BlogScreen/?id=${it.id}")
                },
                    it
                )
            }
        }
        PullRefreshIndicator(refreshing = refreshing, state = state, modifier = Modifier.align(Alignment.TopCenter))
    }
}