package com.example.campus_bbs.ui.network

import com.example.campus_bbs.Global
import com.example.campus_bbs.ui.network.notification.NotificationResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
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

    @GET("/user/{id}")
    suspend fun getUserById(@Header("Authorization") token: String, @Path("id") id: String): UserResponse
    @POST("/users/update/description")
    suspend fun updateDescription(@Header("Authorization") token: String, @Body body: UserUpdateDescriptionDto)

    @POST("/users/update/username")
    suspend fun updateUserName(@Header("Authorization") token: String, @Body body: UserUpdateUsernameDto)

    @Multipart
    @POST("/users/update/avatar")
    suspend fun updateUserAvatar(@Header("Authorization") token: String, @Part images: MultipartBody.Part)

    @GET("/users/notifications")
    suspend fun getUserNotification(@Header("Authorization") token: String): NotificationResponse

    @POST("/users/notification/clear")
    suspend fun clearAllNotification(@Header("Authorization") token: String)

    @POST("/users/notification/clear/{id}")
    suspend fun clearNotificationById(@Header("Authorization") token: String, @Path("id") id: String)

    @POST("/users/update/password")
    suspend fun updateUserPassword(@Header("Authorization") token: String, @Body body: UserUpdatePasswordDto)

    @POST("/users/{id}/subscribe")
    suspend fun subscribe(@Header("Authorization") token: String, @Path("id") id: String)

    @POST("/users/{id}/unsubscribe")
    suspend fun unsubscribe(@Header("Authorization") token: String, @Path("id") id: String)

    @GET("/users/search/findAllByUsernameContaining")
    suspend fun searchUser(@Header("Authorization") token: String, @Query("keyword") keyword: String): SearchUserResponse

    @POST("users/{id}/black")
    suspend fun black(@Header("Authorization") token: String, @Path("id") id: String)

    @POST("users/{id}/unblack")
    suspend fun unblack(@Header("Authorization") token: String, @Path("id") id: String)
}

object UserApi {
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}