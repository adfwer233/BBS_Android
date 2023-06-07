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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
    private lateinit var currentUser: User

    lateinit var currentUserState: MutableStateFlow<User>

    var jwtToken: String = ""

    init {
        viewModelScope.launch {
            jwtToken = dataStore.data.map { it[JWT_TOKEN_KEY] ?: "" }.first()
            currentUserFlow.flowOn(Dispatchers.Default).collect() {
                currentUser = it
                currentUserState = MutableStateFlow(currentUser)
            }
        }
    }

    fun getCurrentUser() {
        Log.e("get current user", "get user")
        viewModelScope.launch {
            currentUserFlow
                .flowOn(Dispatchers.Default)
                .catch {

                }
                .collect {
                    Log.e("user", it.toString())
                    currentUserState.update { it }
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
            followList = listOf(),
            favorBlogList = listOf(),
        )
        emit(currentUser)
    }

    fun updateUserImageUri(uri: String) {
        currentUserState.update { it -> it.copy(userIconUrl = uri) }
    }
}