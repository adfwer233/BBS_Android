package com.example.campus_bbs.ui

import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.*
import com.example.campus_bbs.ui.model.*
import com.example.campus_bbs.ui.network.UserApi
import com.example.campus_bbs.utils.showBasicNotification
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun notificationScreen(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    index: Int = -1
) {
    val communicationViewModel: CommunicationViewModel = viewModel(LocalContext.current as ComponentActivity)

    if (index >= 0) {
        communicationViewModel.openChat(index)
        mainAppNavController.navigate("CommunicationScreen")
    }

    val titles = listOf("Chat", "Notification")
    val pagerState = rememberPagerState(0)

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxHeight()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index, 0F)
                                }
                            },
                            modifier = Modifier
                        ) {
                            Text(text = title, modifier = Modifier.padding(10.dp))
                        }
                    }
                }
            }
        }

        HorizontalPager(
            pageCount = titles.size,
            state = pagerState
        ) { page ->
            when(page) {
                0 -> ChatTab(mainAppNavController = mainAppNavController, modifier = Modifier)
                1 -> NotificationTab()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationTab(
    modifier: Modifier = Modifier
) {
    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity)
    val navControlViewModel: NavControlViewModel = viewModel(LocalContext.current as ComponentActivity)
    val uiState = notificationViewModel.uiState.collectAsState()
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val visitingUserHomeViewModel: VisitingUserHomeViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val scope = rememberCoroutineScope()

    Box(modifier = modifier
        .fillMaxHeight())
    {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(uiState.value.notificationList) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(it.sourceUserMeta.userIconUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "test image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(CircleShape)
                                .height(65.dp)
                                .width(65.dp)
                                .clickable {
                                    scope.launch {
                                        val resp = UserApi.retrofitService.getUserById(
                                            loginViewModel.jwtToken,
                                            it.sourceUserMeta.userId
                                        )
                                        visitingUserHomeViewModel.updateVisitingUser(resp.getUser())
                                        navControlViewModel.userHome = resp.getUser()
                                        navControlViewModel.mainNavController.navigate("UserHome")
                                    }
                                }
                        )
                        Column(
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Column(){
                                Text(text = it.sourceUserMeta.userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(text = it.title)
                                Text(text = it.content)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChatTab(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity)
    val navControlViewModel: NavControlViewModel = viewModel(LocalContext.current as ComponentActivity)
    val uiState = notificationViewModel.uiState.collectAsState()
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()

    val localContext = LocalContext.current

    fun refresh () = refreshScope.launch {
        refreshing = true
        delay(500)
        notificationViewModel.updateUserChat(localContext)
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(modifier = modifier
        .fillMaxHeight()
        .pullRefresh(state)) {
        LazyColumn(
            modifier = Modifier.fillMaxHeight()
//        verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(uiState.value.chatList.size) {
                MessageItemInScreen(mainAppNavController, index = it)
            }
        }
        PullRefreshIndicator(refreshing = refreshing, state = state, modifier = Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageItemInScreen(
    mainAppNavController: NavHostController,
    index: Int,
    modifier: Modifier = Modifier
) {
    val communicationViewModel: CommunicationViewModel = viewModel(LocalContext.current as ComponentActivity)
    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity)
    val uiState = notificationViewModel.uiState.collectAsState()

    val chat = uiState.value.chatList.get(index)

    Card(
        shape = RectangleShape,
        onClick = {
            communicationViewModel.openChat(index)
            mainAppNavController.navigate("CommunicationScreen/?index="+index)
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