package com.example.campus_bbs.ui.state

import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User

data class NotificationUiState(
    val subscribedUserList: List<User> = FakeDataGenerator().generateUserList(10),
    val blogListOfSubscribedUsers: List<Blog> = FakeDataGenerator().generateFakeBlogs(10)
)