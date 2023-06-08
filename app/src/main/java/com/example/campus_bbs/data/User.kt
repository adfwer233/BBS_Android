package com.example.campus_bbs.data

// meta data of user
data class UserMeta(
    val userId: String,
    val userName: String,
    val userIconUrl: String,
)

// full data of user
data class User (
    val userId: String,
    val userName: String,
    var userIconUrl: String,
    val profile: String,
    val followList: List<UserMeta>,
    val favorBlogList: List<Blog>
)