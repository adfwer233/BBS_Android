package com.example.campus_bbs.ui.state

import androidx.compose.ui.text.input.TextFieldValue

data class LoginUiState (
    val email: TextFieldValue = TextFieldValue(),
    val username: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue(),
    val repeatedPassword: TextFieldValue = TextFieldValue()
)