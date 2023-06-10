package com.example.campus_bbs.ui.state

import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator

data class BlogUiState(
    val blog: Blog = FakeDataGenerator().generateSingleFakeBlog(),
    var comment: String = ""
)