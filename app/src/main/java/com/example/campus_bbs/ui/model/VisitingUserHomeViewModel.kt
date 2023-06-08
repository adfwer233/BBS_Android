package com.example.campus_bbs.ui.model

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VisitingUserHomeViewModel(): ViewModel() {
    private val currentUser: User = FakeDataGenerator().generateSingleUser()

    private val _currentUserState = MutableStateFlow(currentUser)
    val currentUserState = _currentUserState.asStateFlow()

    fun updateVisitingUser(user: User) {
        _currentUserState.update { user }
    }
}