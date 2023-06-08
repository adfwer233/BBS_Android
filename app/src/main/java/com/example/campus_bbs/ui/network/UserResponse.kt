package com.example.campus_bbs.ui.network

import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.User
import com.example.campus_bbs.data.UserMeta

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
) {

    fun getUser(): User {
        return User(
            userId = id,
            userName = username,
            userIconUrl = avatarUrl,
            profile = description,
            followList = followList.map { UserMeta(it.userId, it.userName, it.userIconUrl) },
            favorBlogList = listOf()
        )
    }
}
