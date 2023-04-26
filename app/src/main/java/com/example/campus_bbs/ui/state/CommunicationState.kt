package com.example.campus_bbs.ui.state

import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.MessageInfo
import com.example.campus_bbs.data.UserMeta

data class CommunicationState (
    val selfUserMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    val targetUserMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    val messageList: List<MessageInfo> = FakeDataGenerator().generateFakeMessageInfoList(20, selfUserMeta, targetUserMeta)
)