package com.example.campus_bbs.ui.state

data class CreateBlogUiState(
    val titleText: String = "",
    val contentText: String = "",
    val imageUrlList: List<String> = listOf(),
    val videoUrl: String = "",
    val location: String = "",
    val tagInput: String = "",
    val tagList: List<String> = listOf(),
    val progress: Float = 0f,
    val progressBarVisible: Boolean = false
)