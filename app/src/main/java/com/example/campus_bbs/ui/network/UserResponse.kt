package com.example.campus_bbs.ui.network

@kotlinx.serialization.Serializable
data class UserResponse (
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val description: String,
    val avatarUrl: String,
    val createTime: String,
    val lastLoginTime: String,
    val banned: String,
    val followList: List<String>,
    val subscriberList: List<String>,
    val postList: List<String>,
    val interestedTags: List<String>,
    val roles: List<String>,
    val nickname: String
)