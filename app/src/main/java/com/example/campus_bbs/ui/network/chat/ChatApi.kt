package com.example.campus_bbs.ui.network.chat

import com.example.campus_bbs.Global
import com.example.campus_bbs.data.Chat
import com.example.campus_bbs.ui.network.IsLoginResponse
import com.example.campus_bbs.ui.network.LoginDTO
import com.example.campus_bbs.ui.network.LoginResponse
import com.example.campus_bbs.ui.network.RegisterDTO
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL = Global.BASE_HTTP_URL


//private val myJson: Json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ChatApiService {
    @GET("/chat/userchat")
    suspend fun getUserChat(@Header("Authorization") token: String): GetUserChatResponse
}

object ChatApi {
    val retrofitService: ChatApiService by lazy {
        retrofit.create(ChatApiService::class.java)
    }
}