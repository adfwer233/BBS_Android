package com.example.campus_bbs.ui.model

import androidx.lifecycle.ViewModel
import com.example.campus_bbs.Managers.CameraImageManager

class CameraViewModel:ViewModel() {
    var imageManager = CameraImageManager()
}