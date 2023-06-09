package com.example.campus_bbs.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.CollectList
import com.example.campus_bbs.PostsList
import com.example.campus_bbs.data.Chat
import com.example.campus_bbs.ui.model.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UserHomeScreen(
    modifier: Modifier = Modifier,
    mainAppNavController: NavHostController = rememberNavController()
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "")},
                navigationIcon = {
                    IconButton(onClick = { mainAppNavController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("拉黑") },
                            onClick = { /* Handle edit! */ },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            )
        }
    ) {
        contentPadding -> UserHome(modifier = modifier.padding(contentPadding))
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserHome(
    modifier: Modifier = Modifier
) {

    val visitingUserViewModel: VisitingUserHomeViewModel =
        viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val userState = visitingUserViewModel.currentUserState.collectAsState()

    val communicationViewModel: CommunicationViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    var fanSubScreenViewModel : FanSubScreenViewModel = viewModel(LocalContext.current as ComponentActivity)
    val navControlViewModel: NavControlViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val notificationViewModel:NotificationViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)


    Column(
        modifier = modifier.fillMaxHeight(),
//            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(navControlViewModel.userHome.userIconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "test image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(80.dp)
                        .width(80.dp)
                )
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        text = navControlViewModel.userHome.userName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Text(
                        modifier = Modifier,
                        text = userState.value.profile,
                        fontWeight = FontWeight.Light,
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
            Row() {

                OutlinedButton(onClick = { }, modifier=Modifier.weight(6f)) {
                    Text(text = "Subscribe")
                }
                Spacer(modifier = Modifier.width(3.dp))
                OutlinedIconButton(
                    onClick = {
                        var chatList = notificationViewModel.uiState.value.chatList.toMutableList()
                        var matchedChat = chatList.find { it.targetUserMeta.userId == navControlViewModel.userHome.userId }
                        matchedChat?.let {
                            val index = chatList.indexOf(matchedChat)
                            navControlViewModel.mainNavController.navigate("CommunicationScreen/?index=${index}")
                        } ?: let {

                            for (i in 0 until chatList.size) {
                                if (chatList[i].targetUserMeta.userId > navControlViewModel.userHome.userId) {
                                    chatList.add(i, Chat(userViewModel.currentUserState.value.getMeta() , navControlViewModel.userHome.getMeta(), listOf(), 0))
                                    notificationViewModel.updateChatList(chatList)
                                    navControlViewModel.mainNavController.navigate("CommunicationScreen/?index=${i}")
                                    break
                                }
                            }
                            chatList.add(Chat(userViewModel.currentUserState.value.getMeta() , navControlViewModel.userHome.getMeta(), listOf(), 0))
                            notificationViewModel.updateChatList(chatList)
                            navControlViewModel.mainNavController.navigate("CommunicationScreen/?index=0")
                        }
                    })
                {
                    Icon(imageVector = Icons.Default.Email, contentDescription = "setting", modifier = Modifier.weight(1f))
                }
            }
        }

        Row(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clickable {
                        fanSubScreenViewModel.user = navControlViewModel.userHome
                        navControlViewModel.mainNavController.navigate("fansScreen")
                    },
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.Blue,
                    text = navControlViewModel.userHome.followList.size.toString(),
                    fontSize = 15.sp
                )
                Text(
                    text = "关注",
                    fontSize = 15.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().clickable {
                    fanSubScreenViewModel.user = navControlViewModel.userHome
                    navControlViewModel.mainNavController.navigate("subscriberScreen")
                },
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.Blue,
                    text = navControlViewModel.userHome.subscriberList.size.toString(),
                    fontSize = 15.sp
                )
                Text(
                    text = "粉丝",
                    fontSize = 15.sp
                )
            }
        }


        val items = listOf("Liked", "Bookmark")

        var pagerState = rememberPagerState(0)

        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Box {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                    ) {
                        items.forEachIndexed { index, title ->
                            Tab(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    coroutineScope.launch {
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
                pageCount = items.size,
                state = pagerState,
                modifier = Modifier.fillMaxHeight()
            ) { page ->
                when(page) {
                    1 -> CollectList(mainAppNavController = navControlViewModel.mainNavController as NavHostController, user = navControlViewModel.userHome)
                    0 -> PostsList(mainAppNavController = navControlViewModel.mainNavController as NavHostController, user = navControlViewModel.userHome)
                }
            }
        }
    }
}