package com.example.campus_bbs.data

import kotlin.random.Random

data class Chat(
    val selfUserMeta: UserMeta,
    val targetUserMeta: UserMeta,
    val messageInfoList: List<MessageInfo> = FakeDataGenerator().generateFakeMessageInfoList(10, selfUserMeta, targetUserMeta),
    val numberOfUnread: Number = Random.nextInt(0, 3)
)