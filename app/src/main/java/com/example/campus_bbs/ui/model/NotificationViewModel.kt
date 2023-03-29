package com.example.campus_bbs.ui.model

import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.state.NotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    fun updateBlogList() {
        val newList = FakeDataGenerator().generateFakeBlogs(10)
        _uiState.update { currentState -> currentState.copy(blogListOfSubscribedUsers = newList) }
    }
}