package com.example.campus_bbs.ui.model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.ui.state.BlogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BlogViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(BlogUiState())
    val uiState = _uiState.asStateFlow()

    fun updateBlog(newBlog: Blog) {
        Log.i("blog update", newBlog.blogTitle)
        _uiState.update { currentState -> currentState.copy(blog = newBlog) }
    }

    fun getBlog():Blog {
        return uiState.value.blog
    }

    fun updateComment(comment: String) {
        _uiState.update { it.copy(comment = comment) }
    }
}