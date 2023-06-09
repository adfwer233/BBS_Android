package com.example.campus_bbs.ui.state

data class EditProfileUiState (
    val newName: String = "",
    val newProfile: String = "",
    val previousPassword: String = "",
    val newPassword: String = "",
    val repeatedNewPassword : String= ""
)