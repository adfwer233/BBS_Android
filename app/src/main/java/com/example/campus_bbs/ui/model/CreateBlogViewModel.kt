package com.example.campus_bbs.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.campus_bbs.ui.state.BlogUiState
import com.example.campus_bbs.ui.state.CreateBlogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateBlogViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CreateBlogUiState())
    val uiState = _uiState.asStateFlow()

    fun updateTitleText(newTitle: String) {
        _uiState.update { currentState -> currentState.copy(titleText = newTitle) }
    }

    fun updateContentText(newContent: String) {
        _uiState.update { currentState -> currentState.copy(contentText = newContent) }
    }

    fun saveState() {
        _uiState.update { currentState -> currentState.copy(savedContentText = currentState.contentText, savedTitleText = currentState.titleText) }
//        Log.e(uiState.value.titleText, uiState.value.savedTitleText)
    }
}