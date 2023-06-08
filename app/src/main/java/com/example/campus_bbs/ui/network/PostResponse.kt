package com.example.campus_bbs.ui.network
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.data.UserMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Reply (
    var creator:UserMeta,
    var content: String,
    var createTime: Long)
@Serializable
data class post (
    var id: String,
    var creator: UserMeta,
    var createTime: Long,
    var title: String,
    var content: String,
    var images: List<String>,
    var tags: List<String>,
    var likesNumber: Int,
    var collectedNumber: Int,
    var comments: List<Reply>,
    var liked : Boolean,
    var collected: Boolean,
    var location: String,
)
@Serializable
data class GetAllPostResponse(
    val postList: List<post>
)