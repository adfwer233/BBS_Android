package com.example.campus_bbs.ui.state

import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.Chat
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User
import com.example.campus_bbs.ui.network.notification.NotificationVo

data class NotificationUiState(
    val subscribedUserList: List<User> = FakeDataGenerator().generateUserList(10),
    val blogListOfSubscribedUsers: List<Blog> = FakeDataGenerator().generateFakeBlogs(10),

    val chatList: List<Chat> = FakeDataGenerator().generateChatList(10),
    val notificationList: List<NotificationVo> = listOf()
)