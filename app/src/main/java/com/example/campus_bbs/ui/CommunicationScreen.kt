package com.example.campus_bbs.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_bbs.ui.components.MessageReceivedComponent
import com.example.campus_bbs.ui.components.MessageSentComponent
import com.example.campus_bbs.ui.model.CommunicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationScreen(
    communicationViewModel: CommunicationViewModel = CommunicationViewModel(),
    modifier: Modifier = Modifier,
) {
    val uiState = communicationViewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = uiState.value.targetUserMeta.userName )},
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.ArrowBack, "back")
                    }
                }
            )
        }
    ){
       CommunicationList(communicationViewModel = communicationViewModel, modifier = Modifier.padding(it))
    }
}

@Composable
fun CommunicationList(
    communicationViewModel: CommunicationViewModel = CommunicationViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState = communicationViewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxHeight()
    ) {
        items(uiState.value.messageList) {

            if (it.senderUserMeta.userId == uiState.value.selfUserMeta.userId) {
                MessageSentComponent(userMeta = it.senderUserMeta, message = it.messageContent)
            } else {
                MessageReceivedComponent(userMeta = it.senderUserMeta, message = it.messageContent)
            }
        }
    }
}