package com.example.campus_bbs.ui.network.chat

import com.example.campus_bbs.ui.network.UserMetaVo

data class ChatMessge(
    val senderUserMeta: UserMetaVo,
    val receiverUserMeta: UserMetaVo,
    val message: String
)

data class ChatVo(
    val selfUserMeta: UserMetaVo,
    val targetUserMeta: UserMetaVo,
    val messages: List<ChatMessge>
)

data class GetUserChatResponse(
    val userChat: List<ChatVo>
)