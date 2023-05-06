package com.example.campus_bbs

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.campus_bbs.data.CreateBlog
import com.example.campus_bbs.data.repository.CreateBlogRepository
import com.example.campus_bbs.data.repository.CreateBlogSerializer

private val Context.createBlogDataStore: DataStore<CreateBlog> by dataStore(
    fileName = "create_blog.pb",
    serializer = CreateBlogSerializer
)

class BBSApplication: Application() {
    lateinit var createBlogRepository: CreateBlogRepository

    override fun onCreate() {
        super.onCreate()
        createBlogRepository = CreateBlogRepository(createBlogDataStore)
    }
}