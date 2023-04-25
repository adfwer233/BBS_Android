package com.example.campus_bbs.ui.state

import com.example.campus_bbs.data.FakeDataGenerator

data class CreateBlogUiState(
    val savedTitleText: String = "",
    val savedContentText: String = "",
    val titleText: String = "",
    val contentText: String = "",
    val imageUrlList: List<String> = listOf(),
    val videoUrl: String = "https://cloud.tsinghua.edu.cn/f/d059ce302d864d7ab9ee/?dl=1"
)