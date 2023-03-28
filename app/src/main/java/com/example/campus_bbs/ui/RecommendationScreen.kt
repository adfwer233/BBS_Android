package com.example.campus_bbs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.MainViewModel

@Composable
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

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            Button(onClick = { mainViewModel.recommendationViewModel.updateBlogList() }) {
                Text(text = "Refresh")
            }
        }

        items(recommendationUiState.blogList) {
            BlogsCard(
                moreButtonOnClick = {
                    mainViewModel.BlogViewModel.updateBlog(it)
                    mainAppNavController.navigate("BlogScreen")
                },
                blog = it
            )
        }
    }
}