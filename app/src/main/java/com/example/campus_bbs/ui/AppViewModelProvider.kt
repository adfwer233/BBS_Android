package com.example.campus_bbs.ui

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.campus_bbs.BBSApplication
import com.example.campus_bbs.ui.model.*

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            BlogViewModel(
                this.createSavedStateHandle()
            )
        }
        initializer {
            CameraViewModel()
        }
        initializer {
            CommunicationViewModel()
        }
        initializer {
            CreateBlogViewModel(bssApplication().createBlogRepository)
        }
        initializer {
            NotificationViewModel()
        }
        initializer {
            RecommendationViewModel()
        }
    }

}

fun CreationExtras.bssApplication(): BBSApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BBSApplication)
