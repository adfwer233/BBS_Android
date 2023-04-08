package com.example.campus_bbs.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User
import com.example.campus_bbs.data.UserMeta
import com.example.campus_bbs.ui.state.BlogUiState
import com.example.campus_bbs.ui.state.CreateBlogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import kotlin.random.Random

class CreateBlogViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CreateBlogUiState())
    val uiState = _uiState.asStateFlow()

    fun updateTitleText(newTitle: String) {
        _uiState.update { currentState -> currentState.copy(titleText = newTitle) }
    }

    fun updateContentText(newContent: String) {
        _uiState.update { currentState -> currentState.copy(contentText = newContent) }
    }

    fun updateImageUrl(newUrlList: List<String>) {
        _uiState.update { currentState -> currentState.copy(imageUrlList = newUrlList) }
    }

    fun generateBlogFromState(): Blog {
        val newBlog = Blog(
            creator = FakeDataGenerator().generateSingleUserMeta(),
            createTime = Date(),
            blogTitle = uiState.value.titleText,
            blogContent = uiState.value.contentText,
            imageUrlList = uiState.value.imageUrlList,
            blogComments = listOf(),
            subscribed = false,
            liked = false,
            subscribedNumber = 0,
            likedNumber = 0,
            tag = listOf(),
            division = ""
        )

        _uiState.update { CreateBlogUiState() }
        return newBlog
    }

    fun saveState() {
        _uiState.update { currentState -> currentState.copy(savedContentText = currentState.contentText, savedTitleText = currentState.titleText) }
//        Log.e(uiState.value.titleText, uiState.value.savedTitleText)
    }
}