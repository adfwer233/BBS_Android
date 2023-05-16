package com.example.campus_bbs.ui.network

@kotlinx.serialization.Serializable
data class LoginDTO(
    var username: String,
    var password: String
)

@kotlinx.serialization.Serializable
data class RegisterDTO(
    val username: String,
    val password: String
)
