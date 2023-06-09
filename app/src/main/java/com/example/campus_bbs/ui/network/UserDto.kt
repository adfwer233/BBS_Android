package com.example.campus_bbs.ui.network


data class UserUpdateDescriptionDto (val description: String)

data class UserUpdateUsernameDto (val username: String)

data class UserUpdatePasswordDto(val oldPassword: String, val newPassword: String)