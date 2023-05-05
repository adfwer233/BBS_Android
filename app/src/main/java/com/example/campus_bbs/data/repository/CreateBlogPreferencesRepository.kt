package com.example.campus_bbs.data.repository

import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import com.example.campus_bbs.data.CreateBlog
import com.example.campus_bbs.ui.state.CreateBlogUiState
import java.io.InputStream
import java.io.OutputStream

object CreateBlogSerializer : Serializer<CreateBlog> {
    override val defaultValue: CreateBlog = CreateBlog.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CreateBlog {
        try {
            return CreateBlog.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: CreateBlog,
        output: OutputStream
    ) = t.writeTo(output)
}


class CreateBlogRepository(
    private val dataStore: DataStore<CreateBlog>
) {
    private companion object {
        val TITLE_TEXT = stringPreferencesKey("TITLE_TEXT")
        const val TAG = "CreateBlogPreferencesRepo"
    }

    val getCreateBlogUiState: Flow<CreateBlogUiState> = dataStore.data
        .map {
            CreateBlogUiState(
                titleText = it.titleText,
                contentText = it.contentText,
                imageUrlList = it.imageUrlList,
                videoUrl = it.videoUrl
            )
        }

    suspend fun saveTitleText(titleText: String) {
        dataStore.updateData {
            it.toBuilder()
                .setTitleText(titleText)
                .build()
        }
    }

    suspend fun saveCreateBlogUiState(createBlogUiState: CreateBlogUiState) {
        dataStore.updateData {
            it.toBuilder()
                .setTitleText(createBlogUiState.titleText)
                .setContentText(createBlogUiState.contentText)
                .setVideoUrl(createBlogUiState.videoUrl)
                .clearImageUrl()
                .addAllImageUrl(createBlogUiState.imageUrlList)
                .build()
        }
    }
}