package com.example.campus_bbs.ui.network

import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL =
    "http://183.172.141.89:8080"


private val myJson: Json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(myJson.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface PostApiService {
    @POST("/posts")
    suspend fun createPost(@Header("Authorization") token: String, @Body body: CreatePostDTO)

    @GET("/posts")
    suspend fun getAllPost(@Header("Authorization") token: String): GetAllPostResponse
}

object PostApi {
    val retrofitService: PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}