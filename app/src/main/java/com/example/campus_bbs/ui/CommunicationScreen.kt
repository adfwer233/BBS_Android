package com.example.campus_bbs.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campus_bbs.ui.components.MessageReceivedComponent
import com.example.campus_bbs.ui.components.MessageSentComponent
import com.example.campus_bbs.ui.model.CommunicationViewModel
import com.example.campus_bbs.ui.model.NotificationViewModel
import com.example.campus_bbs.ui.network.chat.ChatWebSocketRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationScreen(
    mainNavController: NavController,
    modifier: Modifier = Modifier,
    communicationViewModel: CommunicationViewModel = viewModel(LocalContext.current as ComponentActivity),
    index: Int? = -1
) {

    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val uiState = communicationViewModel.uiState.collectAsState()
    val chatState = notificationViewModel.uiState.collectAsState()


    val chatIndex = if ((index == null) || (index < 0)) uiState.value.chatIndex else index
    val chat = chatState.value.chatList[chatIndex]

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = chat.targetUserMeta.userName )},
                navigationIcon = {
                    IconButton(onClick = { mainNavController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Spacer(modifier = Modifier.width(20.dp))

                TextField(value = uiState.value.messageInput, onValueChange = { communicationViewModel.updateMessageInput(it) })
                
                IconButton(onClick = {
                    if (uiState.value.messageInput.isNotEmpty()) {
                        notificationViewModel.sendChatRequest(
                            ChatWebSocketRequest(
                                operation = "send",
                                senderId = chat.selfUserMeta.userId,
                                receiverId = chat.targetUserMeta.userId,
                                message = uiState.value.messageInput,
                                token = ""
                            )
                        )
                        communicationViewModel.updateMessageInput("")
                    }
                }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "send Message")
                }
            }
        }
    ){
       CommunicationList(communicationViewModel = communicationViewModel, modifier = Modifier.padding(it))
    }
}

@Composable
fun CommunicationList(
    modifier: Modifier = Modifier,
    communicationViewModel: CommunicationViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity)
    val uiState = communicationViewModel.uiState.collectAsState()
    val chatState = notificationViewModel.uiState.collectAsState()
    val chat = chatState.value.chatList[uiState.value.chatIndex]

    val lazyListState = rememberLazyListState(0)

    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        state = lazyListState,
        reverseLayout = true
    ) {
        items(chat.messageInfoList.reversed()) {

            if (it.senderUserMeta.userId == chat.selfUserMeta.userId) {
                MessageSentComponent(userMeta = it.senderUserMeta, message = it.messageContent)
            } else {
                MessageReceivedComponent(userMeta = it.senderUserMeta, message = it.messageContent)
            }
        }
    }
}