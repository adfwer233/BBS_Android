package com.example.campus_bbs.ui.model

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.repository.CreateBlogRepository
import com.example.campus_bbs.ui.state.CreateBlogUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class CreateBlogViewModel(
    private val createBlogRepository: CreateBlogRepository
): ViewModel() {

    private val _progressState = MutableStateFlow(0f)
    val progressState = _progressState.asStateFlow()

    private val _uiState = MutableStateFlow(CreateBlogUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { createBlogRepository.getCreateBlogUiState.first() }
        }
    }
    fun updateTitleText(newTitle: String) {
        _uiState.update { currentState -> currentState.copy(titleText = newTitle) }
        viewModelScope.launch {
            createBlogRepository.saveCreateBlogUiState(uiState.value)
        }
    }

    fun updateProgress(value: Float) {
        _progressState.update { value }
    }

    fun updateProgressVisible(value: Boolean) {
        _uiState.update { it.copy(progressBarVisible = value) }
    }

    fun updateContentText(newContent: String) {
        _uiState.update { currentState -> currentState.copy(contentText = newContent) }
        viewModelScope.launch {
            createBlogRepository.saveCreateBlogUiState(uiState.value)
        }
    }

    fun addImageUrl(urlToAdd: List<String>) {
        var urlList = uiState.value.imageUrlList.toMutableList()
        urlList.addAll(urlToAdd)
        _uiState.update { currentState -> currentState.copy(imageUrlList = urlList) }
        viewModelScope.launch {
            createBlogRepository.saveCreateBlogUiState(uiState.value)
        }
    }
    fun updateImageUrl(newUrlList: List<String>) {
        _uiState.update { currentState -> currentState.copy(imageUrlList = newUrlList) }
        viewModelScope.launch {
            createBlogRepository.saveCreateBlogUiState(uiState.value)
        }
    }

    fun updateVideoUri(newVideoUri: String) {
        _uiState.update { currentState -> currentState.copy(videoUrl = newVideoUri) }
        viewModelScope.launch {
            createBlogRepository.saveCreateBlogUiState(uiState.value)
        }
    }

    fun insertTag(tag: String) {
        val tabList = uiState.value.tagList.toMutableList()
        tabList.add(tag)
        _uiState.update { it.copy(tagList = tabList) }
    }

    fun clearTag() {
        _uiState.update { it.copy(tagInput = "") }
    }

    fun removeVideoUri() {
        updateVideoUri("")
    }

    fun updateLocation(location: String) {
        _uiState.update { currentState -> currentState.copy(location = location) }
        viewModelScope.launch {
            createBlogRepository.saveCreateBlogUiState(uiState.value)
        }
    }

    fun clearUiState() {
        viewModelScope.launch {
            createBlogRepository.cleanCreateBlogUiState()
        }
    }

    fun updateTagInput(tag: String) {
        _uiState.update { it.copy(tagInput = tag) }
    }

    fun generateBlogFromState(): Blog {
        val newBlog = Blog(
            id = "",
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
            tag = uiState.value.tagList,
            location = uiState.value.location
        )

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

}