package com.example.campus_bbs.ui.model

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.UserMeta
import com.example.campus_bbs.ui.AppViewModelProvider
import com.example.campus_bbs.ui.network.CreatePostDTO
import com.example.campus_bbs.ui.network.PostApi
import com.example.campus_bbs.ui.state.RecommendationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import java.util.stream.Collectors

class RecommendationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState: StateFlow<RecommendationUiState> = _uiState.asStateFlow()


    fun updateBlogList(token: String) {
        viewModelScope.launch { try {
            var response = PostApi.retrofitService.getAllPost(token)
            var newList = response.postList.stream().map { post ->
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
                    )}.collect(Collectors.toList()),
                    post.liked,
                    post.collected,
                    post.location
                )
            }.collect(Collectors.toList())
            _uiState.update { currentState -> currentState.copy(blogList = newList) }
        } catch (e: Exception) {
            Log.e("Get All Post", e.toString())
        } }
    }

    fun pushFront(newBlog: Blog) {
        var newList = _uiState.value.blogList.toMutableList()
        newList.add(0, newBlog)
        _uiState.update { currentState -> currentState.copy(blogList = newList.toList()) }
    }
}