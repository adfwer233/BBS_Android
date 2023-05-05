package com.example.campus_bbs.ui

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
            CreateBlogViewModel()
        }
        initializer {
            NotificationViewModel()
        }
        initializer {
            RecommendationViewModel()
        }
    }

}