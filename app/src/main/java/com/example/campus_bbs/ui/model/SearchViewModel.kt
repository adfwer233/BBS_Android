package com.example.campus_bbs.ui.model

import androidx.lifecycle.ViewModel
import com.example.campus_bbs.ui.components.SearchUiState
import com.example.campus_bbs.ui.network.GetAllPostResponse
import com.example.campus_bbs.ui.network.UserResponseList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    fun updateInput(input: String) {
        _uiState.update { it.copy(input = input) }
    }

    fun updateResultList(result: GetAllPostResponse) {
        _uiState.update { it.copy(result = result) }
    }

    fun updateUserResultList(result: UserResponseList) {
        _uiState.update { it.copy(userResult = result) }
    }
}