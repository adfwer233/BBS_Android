package com.example.campus_bbs.ui.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_bbs.JWT_TOKEN_KEY
import com.example.campus_bbs.data.*
import com.example.campus_bbs.ui.network.UserApi
import com.example.campus_bbs.ui.state.EditProfileUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.stream.Collectors

class UserViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
    private val currentUser: User = FakeDataGenerator().generateSingleUser()

    val currentUserState = MutableStateFlow(currentUser)
    var jwtToken: String = ""


    val _editProfileUiState = MutableStateFlow(EditProfileUiState())
    val editProfileUiState = _editProfileUiState.asStateFlow()

    init {
        viewModelScope.launch {
            jwtToken = dataStore.data.map { it[JWT_TOKEN_KEY] ?: "" }.first()
        }
    }

    fun updateUserName(newUserName: String) {
        _editProfileUiState.update { it.copy(newName = newUserName) }
    }

    fun updateProfile(newProfile: String) {
        _editProfileUiState.update { it.copy(newProfile = newProfile) }
    }

    fun getCurrentUser() {
        Log.e("get current user", "get user")
        viewModelScope.launch {
            jwtToken = dataStore.data.map { it[JWT_TOKEN_KEY] ?: "" }.first()
            currentUserFlow
                .flowOn(Dispatchers.Default)
                .catch {
                    Log.e("Error", it.stackTraceToString())
                }
                .collect {user ->
                    Log.e("user", user.toString())
                    currentUserState.update { user }
                    _editProfileUiState.update { it.copy(newName = user.userName, newProfile = user.profile) }
                }
        }
    }

    private val currentUserFlow: Flow<User> = flow {
        Log.e("user", jwtToken)
        val currentUserResponse = UserApi.retrofitService.getCurrentUser(jwtToken)
        Log.e("sadf", currentUserResponse.toString())
        val currentUser = User(
            userId = currentUserResponse.id,
            userName = currentUserResponse.username,
            userIconUrl = currentUserResponse.avatarUrl,
            profile = currentUserResponse.description,
            followList = currentUserResponse.followList.map { UserMeta(it.userId, it.userName, it.userIconUrl) },
            favorBlogList = currentUserResponse.collection.map { post ->
                Blog(
                    post.id,
                    post.creator,
                    Date(post.createTime),
                    post.title,
                    post.content,
                    post.images,
                    post.tags,
                    post.likesNumber,
                    post.collectedNumber,
                    post.comments.stream().map{ reply -> BlogComment(
                        reply.creator,
                        reply.content,
                        Date(reply.createTime),
                    )
                    }.collect(Collectors.toList()),
                    post.liked,
                    post.collected,
                    post.location
                )
            },
            postBlogList = currentUserResponse.postList.map { post ->
                Blog(
                    post.id,
                    post.creator,
                    Date(post.createTime),
                    post.title,
                    post.content,
                    post.images,
                    post.tags,
                    post.likesNumber,
                    post.collectedNumber,
                    post.comments.stream().map { reply ->
                        BlogComment(
                            reply.creator,
                            reply.content,
                            Date(reply.createTime),
                        )
                    }.collect(Collectors.toList()),
                    post.liked,
                    post.collected,
                    post.location
                )
            },
            subscriberList = currentUserResponse.subscriberList.map { UserMeta(it.userId, it.userName, it.userIconUrl) },
        )
        emit(currentUser)
    }

    fun updateUserImageUri(uri: Uri, context: Context) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val imageBitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
        val stream = ByteArrayOutputStream()
        imageBitmap.asAndroidBitmap()
            .compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val requestBody = stream.toByteArray()
            .toRequestBody(
                "image/*".toMediaType()
            )
        val image = MultipartBody.Part.createFormData(
                "image",
                "uploaded.jpeg",
                requestBody
        )

        viewModelScope.launch {
            UserApi.retrofitService.updateUserAvatar(jwtToken, image)
            getCurrentUser()
        }
    }
}