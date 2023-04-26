package com.example.campus_bbs.data

data class MessageInfo(
    val senderUserMeta: UserMeta,
    val receiverUserMeta: UserMeta,
    val messageContent: String,
)