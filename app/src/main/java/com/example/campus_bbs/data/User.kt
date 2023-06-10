package com.example.campus_bbs.data
import kotlinx.serialization.Serializable

// meta data of user
@Serializable
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
    val favorBlogList: List<Blog>,
    val postBlogList: List<Blog>,
    val subscriberList: List<UserMeta>
) {
    fun getMeta(): UserMeta {
        return UserMeta(
            userId = userId,
            userName = userName,
            userIconUrl = userIconUrl
        )
    }
}