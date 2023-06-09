package com.example.campus_bbs.ui.model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User

class FanSubScreenViewModel: ViewModel() {
    var user: User = FakeDataGenerator().generateSingleUser()
}