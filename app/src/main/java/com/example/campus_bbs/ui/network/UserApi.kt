package com.example.campus_bbs.ui.network

import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL = "http://183.172.141.89:8080"

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
}

object UserApi {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}