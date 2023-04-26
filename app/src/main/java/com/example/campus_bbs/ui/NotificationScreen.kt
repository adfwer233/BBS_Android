package com.example.campus_bbs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.*
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.MainViewModel

@Composable
fun notificationScreen(
    mainAppNavController: NavHostController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = mainViewModel.notificationViewModel.uiState

    LazyColumn(
        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            subscribeList()
            Spacer(modifier = Modifier.height(2.dp))
        }

        items(uiState.value.chatList) {
            MessageItemInScreen(mainAppNavController, mainViewModel, chat = it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageItemInScreen(
    mainAppNavController: NavHostController,
    mainViewModel: MainViewModel,
    chat: Chat,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RectangleShape,
        onClick = {
            mainViewModel.communicationViewModel.openChat(chat)
            mainAppNavController.navigate("CommunicationScreen")
        }
    ) {
        Row(
            modifier = Modifier.padding(5.dp)
        ) {
            Spacer(modifier = Modifier.width(5.dp))
            BadgedBox(
                badge = {
                    if (chat.numberOfUnread.toInt() > 0) {
                        Badge {
                            Text(
                                chat.numberOfUnread.toString(),
                                modifier = Modifier.semantics {
                                    contentDescription =
                                        "new notifications"
                                }
                            )
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(chat.targetUserMeta.userIconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "test image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .height(45.dp)
                        .width(45.dp)
                )
            }
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = chat.targetUserMeta.userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
                if (chat.messageInfoList.isNotEmpty()) {
                    Text(
                        text = chat.messageInfoList.last().messageContent,
                        modifier = Modifier.padding(5.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Divider()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun subscribeList(
    subscribeUserList: List<User> = FakeDataGenerator().generateUserList(10),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column {
            Text(text = "Subscribed Users", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                item {
                    Spacer(Modifier.width(3.dp))
                }
                items(subscribeUserList) {
                    Column {
                        Image(
                            imageVector = Icons.Filled.Face,
                            contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(text = it.userName)
                    }
                }
            }
        }
    }
}