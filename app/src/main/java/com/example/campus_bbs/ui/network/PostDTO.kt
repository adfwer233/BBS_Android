package com.example.campus_bbs.ui.network

@kotlinx.serialization.Serializable
data class CreatePostDTO (
    var title: String,
    var content: String,
    var location: String)
