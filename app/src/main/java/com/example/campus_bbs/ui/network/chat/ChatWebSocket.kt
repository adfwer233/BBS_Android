package com.example.campus_bbs.ui.network.chat

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ChatWebSocketRequest(
    val operation: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val token: String
)

@kotlinx.serialization.Serializable
data class ChatWebSocketResponse(
    val senderId: String,
    val message: String
)