package com.example.campus_bbs.ui.network

import com.example.campus_bbs.Global
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = Global.BASE_HTTP_URL

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface UserApiService {
    @GET("/user")
    suspend fun getCurrentUser(@Header("Authorization") token: String): UserResponse

    @POST("/users/update/description")
    suspend fun updateDescription(@Header("Authorization") token: String, @Body body: UserUpdateDescriptionDto)

    @POST("/users/update/username")
    suspend fun updateUserName(@Header("Authorization") token: String, @Body body: UserUpdateUsernameDto)

    @Multipart
    @POST("/users/update/avatar")
    suspend fun updateUserAvatar(@Header("Authorization") token: String, @Part images: MultipartBody.Part)
}

object UserApi {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}