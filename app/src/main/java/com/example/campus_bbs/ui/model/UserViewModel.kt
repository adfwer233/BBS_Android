package com.example.campus_bbs.ui.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus_bbs.JWT_TOKEN_KEY
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User
import com.example.campus_bbs.ui.network.UserApi
import com.example.campus_bbs.ui.state.EditProfileUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
    private val currentUser: User = FakeDataGenerator().generateSingleUser()

    val currentUserState = MutableStateFlow(currentUser)
    var jwtToken: String = ""

    private val _editProfileUiState = EditProfileUiState()
    val editProfileUiState = MutableStateFlow(_editProfileUiState)

    init {
        viewModelScope.launch {
            jwtToken = dataStore.data.map { it[JWT_TOKEN_KEY] ?: "" }.first()
        }
    }

    fun updateUserName(newUserName: String) {
        editProfileUiState.update { it.copy(newName = newUserName) }
    }

    fun updateProfile(newProfile: String) {
        editProfileUiState.update { it.copy(newProfile = newProfile) }
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
                    editProfileUiState.update { it.copy(newName = user.userName, newProfile = user.profile) }
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
            followList = listOf(),
            favorBlogList = listOf(),
        )
        emit(currentUser)
    }

    fun updateUserImageUri(uri: String) {
        currentUserState.update { it -> it.copy(userIconUrl = uri) }
    }
}