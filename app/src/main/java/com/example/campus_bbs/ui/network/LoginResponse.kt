package com.example.campus_bbs.ui.network


@kotlinx.serialization.Serializable
data class LoginResponse(
    val token: String
)

data class IsLoginResponse(val msg: String)