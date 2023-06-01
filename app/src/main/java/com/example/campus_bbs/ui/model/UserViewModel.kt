package com.example.campus_bbs.ui.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class UserViewModel: ViewModel() {
    private val currentUser: User = FakeDataGenerator().generateSingleUser()

    public val currentUserState = MutableStateFlow(currentUser)

    fun updateUserImageUri(uri: String) {
        currentUserState.update { it -> it.copy(userIconUrl = uri) }
    }
}