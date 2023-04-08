package com.example.campus_bbs.ui.model

import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.state.RecommendationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecommendationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(RecommendationUiState())
    val uiState: StateFlow<RecommendationUiState> = _uiState.asStateFlow()

    fun updateBlogList() {
        val newList = FakeDataGenerator().generateFakeBlogs(10)
        _uiState.update { currentState -> currentState.copy(blogList = newList) }
    }

    fun pushFront(newBlog: Blog) {
        var newList = _uiState.value.blogList.toMutableList()
        newList.add(0, newBlog)
        _uiState.update { currentState -> currentState.copy(blogList = newList.toList()) }
    }
}