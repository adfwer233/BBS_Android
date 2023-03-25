package com.example.campus_bbs.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import java.util.*
import kotlin.random.Random

class FakeDataGenerator {
    private fun getRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'.. 'Z') + ('0' ..'9')

        return  (1..length).map { Random.nextInt(0, charPool.size).let { charPool[it] } }.joinToString("")
    }

    fun generateSingleFakeBlog(): Blog {
        return Blog(
            creatorID = Random.nextInt(),
            creatorName = getRandomString(6),
            createTime = Date(),
            blogTitle = getRandomString(15),
            blogContent = getRandomString(500),
            blogComments = listOf<BlogComment>(),
            subscribed = Random.nextBoolean(),
            liked = Random.nextBoolean(),
            subscribedNumber = Random.nextInt(0, 1000),
            likedNumber = Random.nextInt(0, 1000),
            tag = listOf<String>(),
            division = getRandomString(4)
        )
    }

    fun generateFakeBlogs(number: Int): List<Blog> {
        return (1..number).map { generateSingleFakeBlog() }
    }
}

class singleFakeProvider: PreviewParameterProvider<Blog> {
    override val values: Sequence<Blog>
        get() = FakeDataGenerator().generateFakeBlogs(5).asSequence()
}