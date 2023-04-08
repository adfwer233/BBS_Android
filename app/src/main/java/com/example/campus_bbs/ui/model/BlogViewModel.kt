package com.example.campus_bbs.ui.model

import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.ui.state.BlogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BlogViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(BlogUiState())
    val uiState = _uiState.asStateFlow()

    fun updateBlog(newBlog: Blog) {
        _uiState.update { currentState -> currentState.copy(blog = newBlog) }
    }

    fun getBlog():Blog {
        return uiState.value.blog
    }
}