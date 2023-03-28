package com.example.campus_bbs.data

import java.util.*
import kotlin.random.Random

class FakeDataGenerator {
    private fun getRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'.. 'Z') + ('0' ..'9')

        return  (1..length).map { Random.nextInt(0, charPool.size).let { charPool[it] } }.joinToString("")
    }

    fun generateSingleUser(): User {
        return User(
            userId = Random.nextInt(),
            userName = getRandomString(6),
            followList = listOf(),
            favorBlogList = listOf()
        )
    }

    fun generateUserList(number: Int): List<User> {
        return  (1..number).map { generateSingleUser() }
    }

    fun generateSingleComment(depth: Int): BlogComment {
        return BlogComment(
            creator = UserMeta(
                userId = Random.nextInt(),
                userName = getRandomString(6)
            ),
            commentContent = getRandomString(50),
            createTime = Date(),
            followingComment = if (depth == 1) listOf<BlogComment>() else generateCommentList(depth - 1, 3)
        )
    }

    fun generateCommentList(depth: Int, number: Int): List<BlogComment> {
        return (1..number).map { generateSingleComment(depth) }
    }

    fun generateSingleFakeBlog(): Blog {
        return Blog(
            creatorID = Random.nextInt(),
            creatorName = getRandomString(6),
            createTime = Date(),
            blogTitle = getRandomString(15),
            blogContent = getRandomString(500),
            blogComments = generateCommentList(2, 10),
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
