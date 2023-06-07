package com.example.campus_bbs.ui.model

import androidx.compose.ui.text.input.TextFieldValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_bbs.JWT_TOKEN_KEY
import com.example.campus_bbs.ui.state.LoginUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    val tokenFlow: Flow<String> = dataStore.data.map {
        it[JWT_TOKEN_KEY] ?: ""
    }

    var jwtToken: String = ""

    init {
        viewModelScope.launch {
            jwtToken = dataStore.data.map { it[JWT_TOKEN_KEY] ?: "" }.first()
        }
    }

    fun setToken(token: String) {
        jwtToken = token
        viewModelScope.launch {
            dataStore.edit { it[JWT_TOKEN_KEY] = token }
        }
    }



    fun repeatCorrect(): Boolean {
       return uiState.value.password.text == uiState.value.repeatedPassword.text
    }

    fun updateUserName(value: TextFieldValue) {
        _uiState.update { it.copy(username = value) }
    }

    fun updateEmail(value:TextFieldValue) {
        _uiState.update { it.copy(email = value) }
    }

    fun updatePassword(value: TextFieldValue) {
        _uiState.update { it.copy(password = value) }
    }

    fun updateRepeatedPassword(value: TextFieldValue) {
        _uiState.update { it.copy(repeatedPassword = value) }
    }
}