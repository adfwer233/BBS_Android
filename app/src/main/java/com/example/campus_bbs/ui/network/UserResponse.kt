package com.example.campus_bbs.ui.network

@kotlinx.serialization.Serializable
data class UserMetaVo (
    val userId: String,
    val userName: String,
    val userIconUrl: String
)

@kotlinx.serialization.Serializable
data class UserResponse (
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val description: String,
    val avatarUrl: String,
    val createTime: Long,
    val lastLoginTime: Long,
    val banned: String,
    val followList: List<UserMetaVo>,
    val subscriberList: List<UserMetaVo>,
    val postList: List<PostCoverResponse>,
    val interestedTags: List<String>,
    val roles: List<String>,
    val collection: List<PostCoverResponse>
)