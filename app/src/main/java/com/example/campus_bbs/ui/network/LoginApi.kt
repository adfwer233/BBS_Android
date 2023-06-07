package com.example.campus_bbs.ui.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL =
    "http://183.172.178.182:8080"


//private val myJson: Json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface LoginApiService {
    @POST("/login/username")
    suspend fun login(@Body body: LoginDTO): LoginResponse

    @POST("/register/username")
    suspend fun register(@Body body: RegisterDTO)

    @GET("/login/isLogin")
    suspend fun isLogin(@Header("Authorization") token: String): IsLoginResponse
}

object LoginApi {
    val retrofitService: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }
}