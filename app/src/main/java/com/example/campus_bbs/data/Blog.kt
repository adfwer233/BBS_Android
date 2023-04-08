package com.example.campus_bbs.data

import java.util.Date

data class BlogComment (
    val creator: UserMeta,
    val commentContent: String,
    val createTime: Date,
    val followingComment: List<BlogComment>
)

data class Blog (
    val creator: UserMeta,
    val createTime: Date,

    val blogTitle: String,

    // TODO: More data types of content
    val blogContent: String,
    val imageUrlList: List<String>,

    val blogComments: List<BlogComment>,
    
    val subscribed: Boolean,
    val liked: Boolean,
    val subscribedNumber: Int,
    val likedNumber: Int,

    val tag: List<String>,
    val division: String
)
