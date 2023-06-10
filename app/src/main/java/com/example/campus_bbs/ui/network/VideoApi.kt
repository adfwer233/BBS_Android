package com.example.campus_bbs.ui.network

import com.example.campus_bbs.Global
import com.example.campus_bbs.ui.network.notification.NotificationResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*

private const val BASE_URL = Global.BASE_HTTP_URL


private val myJson: Json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(myJson.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface VideoApiService {

//    @Multipart
//    @POST("/file")
//    suspend fun upload(@Header("Authorization") token: String, @Part images: MultipartBody.Part)
//

    @POST("/file/body")
    suspend fun upload(@Header("Authorization") token: String, @Body images: ReqBodyWithProgress)
}

object VideoApi {
    val retrofitService: VideoApiService by lazy {
        retrofit.create(VideoApiService::class.java)
    }
}