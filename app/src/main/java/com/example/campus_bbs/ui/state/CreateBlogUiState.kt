package com.example.campus_bbs.ui.state

import androidx.room.Entity
import com.example.campus_bbs.data.FakeDataGenerator

@Entity(tableName = "create_blog_ui_state")
data class CreateBlogUiState(
    val titleText: String = "",
    val contentText: String = "",
    val imageUrlList: List<String> = listOf(),
    val videoUrl: String = "https://cloud.tsinghua.edu.cn/f/d059ce302d864d7ab9ee/?dl=1"
)