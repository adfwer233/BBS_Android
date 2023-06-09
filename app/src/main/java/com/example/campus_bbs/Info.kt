package com.example.campus_bbs

import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.User
import com.example.campus_bbs.ui.AppViewModelProvider
import com.example.campus_bbs.ui.DefaultRecommendation
import com.example.campus_bbs.ui.HotRecommendation
import com.example.campus_bbs.ui.SubscribedRecommendation
import com.example.campus_bbs.ui.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogsCard(
    moreButtonOnClick : () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Row {
            Icon(Icons.Filled.Face, contentDescription = "user")
            Column {
                Text(text = "Creator")
                Text(text = "Creator Info")
            }
        }

        Text(text = "Card Title")
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = "Card context")

        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = moreButtonOnClick
        ) {
            Text(text = "more")
        }
    }
}

@Composable
fun PostsList(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    user: User
) {
    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(user.postBlogList) {
                com.example.campus_bbs.ui.components.BlogsCard(
                    {
                        println("BlogScreen/?id=${it.id}")
                        mainAppNavController.navigate("BlogScreen/?id=${it.id}")
                    },
                    it
                )
            }
        }
    }
}

@Composable
fun CollectList(
    mainAppNavController: NavHostController,
    modifier: Modifier = Modifier,
    user: User
) {
    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(user.favorBlogList) {
                com.example.campus_bbs.ui.components.BlogsCard(
                    {
                        println("BlogScreen/?id=${it.id}")
                        mainAppNavController.navigate("BlogScreen/?id=${it.id}")
                    },
                    it
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Info (
    mainNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    var fanSubScreenViewModel : FanSubScreenViewModel = viewModel(LocalContext.current as ComponentActivity)
    val userState = userViewModel.currentUserState.collectAsState()

    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userState.value.userIconUrl)
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
                        text = userState.value.userName,
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

                OutlinedButton(onClick = { mainNavController.navigate("EditProfile") }, modifier=Modifier.weight(6f)) {
                    Text(text = "Edit Profile")
                }
                Spacer(modifier = Modifier.width(3.dp))
                OutlinedIconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "setting", modifier = Modifier.weight(1f))
                }
            }
        }

        Row(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clickable {
                        fanSubScreenViewModel.user = userViewModel.currentUserState.value
                        mainNavController.navigate("fansScreen")
                               },
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.Blue,
                    text = userViewModel.currentUserState.value.followList.size.toString(),
                    fontSize = 15.sp
                )
                Text(
                    text = "关注",
                    fontSize = 15.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxWidth(0.5f)
                    .clickable {
                        fanSubScreenViewModel.user = userViewModel.currentUserState.value
                        mainNavController.navigate("subscriberScreen")
                               },
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.Blue,
                    text = userViewModel.currentUserState.value.subscriberList.size.toString(),
                    fontSize = 15.sp
                )
                Text(
                    text = "粉丝",
                    fontSize = 15.sp
                )
            }
        }


        val items = listOf("我的发布", "我的收藏")

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
                    1 -> CollectList(mainAppNavController = mainNavController, user = userViewModel.currentUserState.value)
                    0 -> PostsList(mainAppNavController = mainNavController, user = userViewModel.currentUserState.value)
                }
            }
        }
    }
}