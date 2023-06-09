package com.example.campus_bbs.ui.components

import com.example.campus_bbs.ui.network.GetAllPostResponse

data class SearchUiState(
    val input: String = "",
    val result: GetAllPostResponse = GetAllPostResponse(listOf())
)