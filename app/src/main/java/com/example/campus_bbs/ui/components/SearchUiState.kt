package com.example.campus_bbs.ui.components

import com.example.campus_bbs.ui.network.GetAllPostResponse
import com.example.campus_bbs.ui.network.SearchUserResponse
import com.example.campus_bbs.ui.network.UserResponseList

data class SearchUiState(
    val input: String = "",
    val result: GetAllPostResponse = GetAllPostResponse(listOf()),

    val userResult: SearchUserResponse = SearchUserResponse(UserResponseList(listOf()))
)