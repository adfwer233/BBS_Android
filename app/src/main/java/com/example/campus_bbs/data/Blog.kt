package com.example.campus_bbs.data

import java.util.Date
import kotlinx.serialization.Serializable
data class BlogComment (
    val creator: UserMeta,
    val commentContent: String,
    val createTime: Date,
)

data class Blog (
    val id: String,
    val creator: UserMeta,
    val createTime: Date,
    val blogTitle: String,

    // TODO: More data types of content
    val blogContent: String,
    val imageUrlList: List<String>,
    val tag: List<String>,
    val likedNumber: Int,
    val subscribedNumber: Int,
    val blogComments: List<BlogComment>,
    val liked: Boolean,
    val subscribed: Boolean,
    val location: String = ""
)
