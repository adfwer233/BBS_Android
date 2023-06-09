package com.example.campus_bbs.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.model.SearchViewModel
import com.example.campus_bbs.ui.network.PostApi
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.Collectors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SearchScreen() {

    val scope = rememberCoroutineScope()

    val searchViewModel: SearchViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val loginViewModel: LoginViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val uiState = searchViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Row(
            modifier = Modifier.padding(15.dp)
        ) {
            OutlinedTextField(value = uiState.value.input, onValueChange = {searchViewModel.updateInput(it)})
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedIconButton(onClick = {
                scope.launch {
                    val resp = PostApi.retrofitService.search(
                        loginViewModel.jwtToken,
                        uiState.value.input
                    )
                    searchViewModel.updateResultList(resp)
                }
            }, modifier = Modifier.align(Alignment.CenterVertically).width(45.dp).height(45.dp)) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "")
            }
        }
        LazyColumn() {
            items(uiState.value.result.postList) {post ->
                BlogsCard(blog =
                    Blog(
                        post.id,
                        post.creator,
                        Date(post.createTime),
                        post.title,
                        post.content,
                        post.images,
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