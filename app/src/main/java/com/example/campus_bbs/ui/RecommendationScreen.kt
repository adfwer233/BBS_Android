package com.example.campus_bbs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
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
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.MainViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
fun RecommendationScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = MainViewModel()
) {
//    val blogList = remember{ mutableStateOf<List<Blog>>(FakeDataGenerator().generateFakeBlogs(10)) }

    val recommendationUiState by mainViewModel.recommendationViewModel.uiState.collectAsState()

    if (recommendationUiState.blogList.isEmpty()) {
        mainViewModel.recommendationViewModel.updateBlogList()
    }

    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(1) }
    val refreshScope = rememberCoroutineScope()

    fun refresh () = refreshScope.launch {
        refreshing = true
        delay(1500)
        mainViewModel.recommendationViewModel.updateBlogList()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(modifier = modifier.pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(mainViewModel.recommendationViewModel.uiState.value.blogList) {
                BlogsCard({
                            mainViewModel.BlogViewModel.updateBlog(it)
                            mainAppNavController.navigate("BlogScreen")
                          },
                    it
                )
            }
        }
        PullRefreshIndicator(refreshing = refreshing, state = state, modifier = Modifier.align(Alignment.TopCenter))
    }
}