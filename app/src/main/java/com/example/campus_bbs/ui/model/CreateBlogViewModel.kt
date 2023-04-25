package com.example.campus_bbs.ui.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User
import com.example.campus_bbs.data.UserMeta
import com.example.campus_bbs.ui.state.BlogUiState
import com.example.campus_bbs.ui.state.CreateBlogUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import kotlin.random.Random

class CreateBlogViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CreateBlogUiState())
    val uiState = _uiState.asStateFlow()

    // TODO: Refactor these to uistate
    private val imageUrlListStateFlow = MutableStateFlow(_uiState.value.imageUrlList)
    val imageUrlList: StateFlow<List<String>> = imageUrlListStateFlow

    private val videoUrlListStateFlow = MutableStateFlow(_uiState.value.videoUrl)
    val videoUrlList: StateFlow<String> = videoUrlListStateFlow

    fun updateTitleText(newTitle: String) {
        _uiState.update { currentState -> currentState.copy(titleText = newTitle) }
    }

    fun updateContentText(newContent: String) {
        _uiState.update { currentState -> currentState.copy(contentText = newContent) }
    }

    fun updateImageUrl(newUrlList: List<String>) {
        imageUrlListStateFlow.value = newUrlList
        _uiState.update { currentState -> currentState.copy(imageUrlList = newUrlList) }
    }

    fun updateVideoUri(newVideoUri: String) {
        videoUrlListStateFlow.value = newVideoUri
        _uiState.update { currentState -> currentState.copy(videoUrl = newVideoUri) }
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

        imageUrlListStateFlow.value = listOf()
        _uiState.update { CreateBlogUiState() }
        return newBlog
    }

    fun removeImageUrl(index: Int) {
        var newList = _uiState.value.imageUrlList.toMutableList()
        newList.removeAt(index)
        updateImageUrl(newList)
    }

    fun addRandomImageUrl() {
        val newList = _uiState.value.imageUrlList.toMutableList()
        newList.add(FakeDataGenerator().generateImageUrlList(1)[0])
        updateImageUrl(newList)
    }

    fun saveState() {
        _uiState.update { currentState -> currentState.copy(savedContentText = currentState.contentText, savedTitleText = currentState.titleText) }
//        Log.e(uiState.value.titleText, uiState.value.savedTitleText)
    }
}