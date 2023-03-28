package com.example.campus_bbs.data

// meta data of user
data class UserMeta(
    val userId: Int,
    val userName: String
)

// full data of user
data class User (
    val userId: Int,
    val userName: String,

    val followList: List<UserMeta>,
    val favorBlogList: List<Blog>
)