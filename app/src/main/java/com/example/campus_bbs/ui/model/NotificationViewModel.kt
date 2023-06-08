package com.example.campus_bbs.ui.model

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_bbs.JWT_TOKEN_KEY
import com.example.campus_bbs.data.Chat
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.MessageInfo
import com.example.campus_bbs.data.UserMeta
import com.example.campus_bbs.ui.AppViewModelProvider
import com.example.campus_bbs.ui.network.UserMetaVo
import com.example.campus_bbs.ui.network.chat.ChatApi
import com.example.campus_bbs.ui.network.chat.ChatVo
import com.example.campus_bbs.ui.network.chat.ChatWebSocketRequest
import com.example.campus_bbs.ui.network.chat.WebsocketManager
import com.example.campus_bbs.ui.state.NotificationUiState
import com.example.campus_bbs.utils.showBasicNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val dataStore: DataStore<Preferences>
): ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    private var websocketManager : WebsocketManager? = null
    lateinit var registeredContext: Context
    fun updateBlogList() {
        val newList = FakeDataGenerator().generateFakeBlogs(10)
        _uiState.update { currentState -> currentState.copy(blogListOfSubscribedUsers = newList) }
    }

    var jwtToken: String = ""
    var updateTime = 0

    init {
        viewModelScope.launch {
            jwtToken = dataStore.data.map { it[JWT_TOKEN_KEY] ?: "" }.first()
            userChatFlow
                .flowOn(Dispatchers.Default)
                .catch {
                    Log.e("Error", it.stackTraceToString())
                }
                .collect {listOfChat ->
                    _uiState.update { it.copy(chatList = listOfChat) }
                }
            connect()
        }
        Log.e("asdfasdfasdfasdfasdf asdf qaertgvafqzavvvvvvvvvvvvvvXCJ", jwtToken)

    }
    fun getTotalUnreadNumber() : Number {
        return uiState.value.chatList.sumOf { it.numberOfUnread.toInt() }
    }

    fun sendChatRequest(chatWebSocketRequest: ChatWebSocketRequest) {
        viewModelScope.launch {
            val chatWebSocketRequestWithToken = chatWebSocketRequest.copy(token = jwtToken)
            websocketManager?.send({ updateUserChat() }, chatWebSocketRequestWithToken)
        }
    }

    fun updateChatList(chatList: List<Chat>) {
        _uiState.update { it.copy(chatList = chatList) }
    }

    fun updateUserChat(context: Context? = null) {
        viewModelScope.launch {
            jwtToken = dataStore.data.map { it[JWT_TOKEN_KEY] ?: "" }.first()
            userChatFlow
                .flowOn(Dispatchers.Default)
                .catch {
                    Log.e("Error", it.stackTraceToString())
                }
                .collect {

                    var listOfChat = it

                    if (updateTime > 0) {
                        context?.run {
                            registeredContext = context
                            listOfChat.forEachIndexed { index, newChat ->
                                var found = false
                                _uiState.value.chatList.forEach { oldChat ->
                                    if (newChat.targetUserMeta.userId == oldChat.targetUserMeta.userId) {
                                        newChat.numberOfUnread =
                                            newChat.messageInfoList.size - oldChat.messageInfoList.size
                                        showBasicNotification(
                                            context,
                                            newChat.targetUserMeta.userName,
                                            newChat.messageInfoList.last().messageContent,
                                            index
                                        )
                                        found = true
                                    }
                                }

                                if (!found) {
                                    showBasicNotification(
                                        context,
                                        newChat.targetUserMeta.userName,
                                        newChat.messageInfoList.last().messageContent,
                                        index
                                    )
                                }
                            }
                        }
                    }
                    _uiState.update { it.copy(chatList = listOfChat) }
                    updateTime ++
                }
        }

    }

    private val userChatFlow: Flow<List<Chat>> = flow {
        Log.i("get user chat", "get user chat")
        val getUserChatResponse = ChatApi.retrofitService.getUserChat(jwtToken)
        Log.i("get user chat", getUserChatResponse.toString())

        val res: List<Chat> = getUserChatResponse.userChat.map {
            val selfUserMetaVo: UserMetaVo = it.selfUserMeta
            val selfUserMeta: UserMeta = UserMeta(
                userId = selfUserMetaVo.userId,
                userName = selfUserMetaVo.userName,
                userIconUrl = selfUserMetaVo.userIconUrl
            )

            val targetUserMetaVo: UserMetaVo = it.targetUserMeta
            val targetUserMeta: UserMeta = UserMeta(
                userId = targetUserMetaVo.userId,
                userName = targetUserMetaVo.userName,
                userIconUrl = targetUserMetaVo.userIconUrl
            )


            Chat(
                selfUserMeta = selfUserMeta,
                targetUserMeta = targetUserMeta,
                messageInfoList = it.messages.map { chatMessage ->
                    if (chatMessage.senderUserMeta.userId == selfUserMeta.userId) {
                        MessageInfo(selfUserMeta, targetUserMeta, chatMessage.message)
                    } else {
                        MessageInfo(targetUserMeta, selfUserMeta, chatMessage.message)
                    }
                },
                0
            )
        }
        Log.i("get user chat", res.toString())
        emit(res);
    }

    private fun connect() {
        viewModelScope.launch {
            websocketManager?.close()
            if (jwtToken == "")
                return@launch
            websocketManager = WebsocketManager(jwtToken.replace("Bearer ", ""))
            websocketManager!!.connect(
                jwtToken,
                onConnect = {
//                    viewModelScope.launch {
//                        websocketManager!!.send({ }, ChatWebSocketRequest(operation = "register", senderId = "", "", ""))
//                    }
                },
                onReceive = {
                    Log.e("On RECEIVE", it.toString())
                    updateUserChat(registeredContext)
                }
            )
        }
    }
}