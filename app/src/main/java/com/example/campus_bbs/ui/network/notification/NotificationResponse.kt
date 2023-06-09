package com.example.campus_bbs.ui.network.notification

import com.example.campus_bbs.data.UserMeta


data class NotificationVo (
    val id: String,
    val targetUserMeta: UserMeta,
    val sourceUserMeta: UserMeta,
    val route: String,
    val title: String,
    val content: String,
    val readFlag: Boolean
)

data class NotificationResponse (
    val NotificationList: List<NotificationVo>
)