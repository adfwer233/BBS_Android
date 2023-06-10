package com.example.campus_bbs.ui.network

import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.data.User
import com.example.campus_bbs.data.UserMeta
import java.util.*
import java.util.stream.Collectors

@kotlinx.serialization.Serializable
data class UserMetaVo(
    val userId: String,
    val userName: String,
    val userIconUrl: String
)

@kotlinx.serialization.Serializable
data class UserResponse(
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
    val collection: List<PostCoverResponse>,
    val blackList: List<UserMetaVo>
) {

    fun getUser(): User {
        return User(
            userId = id,
            userName = username,
            userIconUrl = avatarUrl,
            profile = description,
            followList = followList.map { UserMeta(it.userId, it.userName, it.userIconUrl) },
            favorBlogList = collection.map { post ->
                Blog(
                    post.id,
                    post.creator,
                    Date(post.createTime),
                    post.title,
                    post.content,
                    post.images,
                    post.tags,
                    post.likesNumber,
                    post.collectedNumber,
                    post.comments.stream().map { reply ->
                        BlogComment(
                            reply.creator,
                            reply.content,
                            Date(reply.createTime),
                        )
                    }.collect(Collectors.toList()),
                    post.liked,
                    post.collected,
                    post.location
                )
            },
            postBlogList = postList.map { post ->
                Blog(
                    post.id,
                    post.creator,
                    Date(post.createTime),
                    post.title,
                    post.content,
                    post.images,
                    post.tags,
                    post.likesNumber,
                    post.collectedNumber,
                    post.comments.stream().map { reply ->
                        BlogComment(
                            reply.creator,
                            reply.content,
                            Date(reply.createTime),
                        )
                    }.collect(Collectors.toList()),
                    post.liked,
                    post.collected,
                    post.location
                )
            },
            subscriberList = subscriberList.map { UserMeta(it.userId, it.userName, it.userIconUrl) },
            blackList = blackList.map { UserMeta(it.userId, it.userName, it.userIconUrl) }
        )
    }
}

data class UserSearch(
    val userId: String,
    val username: String,
    val avatarUrl: String,
    val description: String
)

data class UserResponseList(val users: List<UserSearch>)

data class SearchUserResponse(
    val _embedded: UserResponseList
)