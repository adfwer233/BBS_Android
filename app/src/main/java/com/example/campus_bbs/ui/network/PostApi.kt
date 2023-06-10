package com.example.campus_bbs.ui.network

import com.example.campus_bbs.Global
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = Global.BASE_HTTP_URL


private val myJson: Json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
private val retrofit = Retrofit.Builder()
    .addConverterFactory(myJson.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface PostApiService {
    @Multipart
    @POST("/posts")
    suspend fun createPost(@Header("Authorization") token: String, @Part("title") title: RequestBody,
        @Part("content") content: RequestBody, @Part("location") location: RequestBody,
        @Part("tag") tag: RequestBody, @Part images: List<MultipartBody.Part>)
//    , @Part videos: List<MultipartBody.Part>

    @GET("/posts")
    suspend fun getAllPost(@Header("Authorization") token: String): GetAllPostResponse

    @GET("/posts/search")
    suspend fun search(@Header("Authorization") token: String, @Query("keyword") keyword: String ) : GetAllPostResponse

    @POST("/posts/{id}/like")
    suspend fun likePost(@Header("Authorization") token: String, @Path("id") id: String)

    @POST("/posts/{id}/unlike")
    suspend fun unlikePost(@Header("Authorization") token: String, @Path("id") id: String)

    @POST("/posts/{id}/collect")
    suspend fun collectPost(@Header("Authorization") token: String, @Path("id") id: String)

    @POST("/posts/{id}/uncollect")
    suspend fun uncollectPost(@Header("Authorization") token: String, @Path("id") id: String)

    @POST("/posts/{id}/reply")
    suspend fun replyPost(@Header("Authorization") token: String, @Path("id") id: String, @Body body: PostReplyDto)
}

object PostApi {
    val retrofitService: PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
}