package com.example.campus_bbs.ui.model

import androidx.lifecycle.ViewModel
import com.example.campus_bbs.ui.state.BlogUiState
import com.example.campus_bbs.ui.state.CommunicationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CommunicationViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CommunicationState())
    val uiState = _uiState.asStateFlow()

    fun updateMessageInput(newInput: String) {
        _uiState.update { currentState -> currentState.copy(messageInput = newInput) }
    }
}