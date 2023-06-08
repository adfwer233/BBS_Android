package com.example.campus_bbs.data

import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class FakeDataGenerator {
    private fun getRandomString(length: Int): String {
        val charPool: List<Char> = ('a'..'z') + ('A'.. 'Z') + ('0' ..'9')

        return  (1..length).map { Random.nextInt(0, charPool.size).let { charPool[it] } }.joinToString("")
    }

    fun generateSingleUserMeta(): UserMeta {
        return UserMeta(
            userId = getRandomString(6),
            userName = getRandomString(6),
            userIconUrl = generateImageUrlList(1)[0],
        )
    }
    fun generateSingleUser(): User {
        return User(
            userId = getRandomString(6),
            userName = getRandomString(6),
            userIconUrl = generateImageUrlList(1)[0],
            profile = getRandomString(10),
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
                userId = getRandomString(6),
                userName = getRandomString(6) ,
                userIconUrl = generateImageUrlList(1)[0],
            ),
            commentContent = getRandomString(50),
            createTime = Date(),
        )
    }

    fun generateCommentList(depth: Int, number: Int): List<BlogComment> {
        return (1..number).map { generateSingleComment(depth) }
    }

    fun generateImageUrlList(number: Int): List<String> {
        val urls = listOf<String>(
            "https://ts4.cn.mm.bing.net/th?id=OIP-C.a13xv-Dkd4pE26B1PNFpyAHaGl&w=265&h=235&c=8&rs=1&qlt=90&o=6&dpr=2&pid=3.1&rm=2",
            "https://ts2.cn.mm.bing.net/th?id=OIP-C.xjxtzuYpyhxD6zo3OV2ZaAHaFj&w=288&h=216&c=8&rs=1&qlt=90&o=6&dpr=2&pid=3.1&rm=2",
            "https://ts3.cn.mm.bing.net/th?id=OIP-C.CBRSObBHaX1dA_OqPdZSDQHaHt&w=244&h=255&c=8&rs=1&qlt=90&o=6&dpr=2&pid=3.1&rm=2",
            "https://ts1.cn.mm.bing.net/th?id=OIP-C.yoIeq6UsWUnvlduoSAOJaAHaI4&w=228&h=273&c=8&rs=1&qlt=90&o=6&dpr=2&pid=3.1&rm=2"
        )

        return (1..number).map { urls[Random.nextInt(0, urls.size)] }
    }

    fun generateSingleFakeBlog(): Blog {
        return Blog(
            id = getRandomString(6),
            creator = UserMeta(
                userId = getRandomString(6),
                userName = getRandomString(6) ,
                userIconUrl = generateImageUrlList(1)[0],
            ),
            createTime = Date(),
            blogTitle = getRandomString(15),
            blogContent = getRandomString(500),
            imageUrlList = generateImageUrlList(Random.nextInt(1, 10)),
            blogComments = generateCommentList(2, 10),
            subscribed = Random.nextBoolean(),
            liked = Random.nextBoolean(),
            subscribedNumber = Random.nextInt(0, 1000),
            likedNumber = Random.nextInt(0, 1000),
            tag = listOf<String>(),
        )
    }

    fun generateFakeBlogs(number: Int): List<Blog> {
        return (1..number).map { generateSingleFakeBlog() }
    }

    fun generateFakeMessageInfoList(number: Int, selfUserMeta: UserMeta, targetUserMeta: UserMeta): List<MessageInfo> {
        val res = (1..number).map {
            val tmp = Random.nextBoolean()
            if (tmp) {
                MessageInfo(
                    senderUserMeta = selfUserMeta,
                    receiverUserMeta = targetUserMeta,
                    messageContent = getRandomString(200)
                )
            } else {
                MessageInfo(
                    senderUserMeta = targetUserMeta,
                    receiverUserMeta = selfUserMeta,
                    messageContent = getRandomString(200)
                )
            }
        }
        return res
    }

    fun generateFakeMessageInfoList(number: Int): List<MessageInfo> {
        val selfUserMeta = generateSingleUserMeta()
        val targetUserMeta = generateSingleUserMeta()
        return generateFakeMessageInfoList(number, selfUserMeta, targetUserMeta)
    }

    fun generateChatList(number: Int): List<Chat> {
        val selfUserMeta = generateSingleUserMeta()
        return (1..number).map {
            val targetUserMeta = generateSingleUserMeta()
            Chat(selfUserMeta, targetUserMeta, generateFakeMessageInfoList(10, selfUserMeta, targetUserMeta))
        }
    }
}
