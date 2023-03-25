package com.example.campus_bbs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.model.RecommendationViewModel

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
            .fillMaxWidth()
    ) {
        
        Row {
            Icon(Icons.Filled.Face, contentDescription = "user")
            Column {
                Text(text = blog.creatorName)
                Text(text = blog.createTime.toString())
            }
        }

        Text(text = blog.blogTitle, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = blog.blogContent)
        
        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = moreButtonOnClick
        ) {
            Text(text = "more")
        }

        Spacer(modifier = Modifier.height(3.dp))

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            IconButton(onClick = { /*TODO*/ }, modifier=Modifier.weight(1F)) {
                Row {
                    Icon(imageVector = Icons.Filled.Add, "test")
                    Text(text = blog.subscribedNumber.toString())
                }
            }
            IconButton(onClick = { /*TODO*/ }, modifier=Modifier.weight(1F)) {
                Row {
                    Icon(imageVector = Icons.Filled.Add, "test")
                    Text(text = blog.blogComments.size.toString())
                }
            }
            IconButton(onClick = { /*TODO*/ }, modifier=Modifier.weight(1F)) {
                Row {
                    Icon(imageVector = Icons.Filled.Add, "test")
                    Text(text = blog.likedNumber.toString())
                }
            }
        }
    }
}

@Composable
fun RecommendationScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    recommendationViewModel: RecommendationViewModel = viewModel()
) {
//    val blogList = remember{ mutableStateOf<List<Blog>>(FakeDataGenerator().generateFakeBlogs(10)) }

    val recommendationUiState by recommendationViewModel.uiState.collectAsState()

    if (recommendationUiState.blogList.isEmpty()) {
        recommendationViewModel.updateBlogList()
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(recommendationUiState.blogList) {
            BlogsCard(moreButtonOnClick = { mainAppNavController.navigate("BlogScreen")}, blog = it)
        }
    }
}