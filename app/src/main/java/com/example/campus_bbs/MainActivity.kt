package com.example.campus_bbs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.ui.*
import com.example.campus_bbs.ui.components.*
import com.example.campus_bbs.ui.model.CameraViewModel
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.model.NotificationViewModel
import com.example.campus_bbs.ui.theme.Campus_BBSTheme
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Campus_BBSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }
}

@Composable
fun App() {
    val mainAppNavController = rememberNavController()

    val cameraViewModel = CameraViewModel()

    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val tokenState = loginViewModel.tokenFlow.collectAsState(loginViewModel.jwtToken)

    NavHost(navController = mainAppNavController, startDestination = "AppHome") {
        composable("AppHome") {
//            OnlineVideoPlayer(videoUrl = "https://cloud.tsinghua.edu.cn/f/d059ce302d864d7ab9ee/?dl=1",)
//            SimpleCameraScreen(cameraViewModel = cameraViewModel)
//            ImagePicker()
//            AppHome(mainAppNavController)
            if (tokenState.value == "")
                LoginScreen()
            else
                AppHome(mainAppNavController)
//            CommunicationScreen()
        }
        composable("BlogScreen") {
            BlogScreen(mainAppNavController)
        }
        composable("UserHome") {
            UserHomeScreen(mainAppNavController = mainAppNavController)
        }
        composable("CreateBlog") {
            CreateBlogScreen(
                mainAppNavController = mainAppNavController,
            )
        }
        composable("fansScreen") {
            FansScreen(mainAppNavController = mainAppNavController)
        }
        composable("CommunicationScreen") {
            CommunicationScreen(mainNavController = mainAppNavController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHome(
    mainAppNavController: NavHostController,
) {
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    val items = listOf("homepage", "Notification", "info")

    val navController = rememberNavController()
    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Campus BBS") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User Icon"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            if (index == 1) {
                                BadgedBox(
                                    badge = {
                                        Badge {
                                            val badgeNumber = notificationViewModel.getTotalUnreadNumber()
                                            Text(
                                                badgeNumber.toString(),
                                                modifier = Modifier.semantics {
                                                    contentDescription =
                                                        "$badgeNumber new notifications"
                                                }
                                            )
                                        }
                                    }) {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Favorite"
                                    )
                                }
                            } else {
                                Icon(Icons.Filled.Favorite, contentDescription = item)
                            }
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            navController.navigate(items[index]) {
                                popUpTo(items[selectedItem]) {
                                    inclusive = true
                                }
                            }
                            selectedItem = index
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mainAppNavController.navigate("CreateBlog") }) {
                Text(text = "+", color = Color.White, fontSize = 26.sp)
            }
        }
    ) { contentPadding ->
        MainAppView(
            mainAppNavController,
            navController,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun MainAppView(
    mainAppNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier
) {

    NavHost(navController = navController, startDestination = "homepage") {
        composable("homepage") {
//            HomePageTest(
//                update = { navController.navigate("blogs") },
//                modifier
//            )
            RecommendationScreen(
                mainAppNavController,
                modifier = modifier,
            )
        }
        composable("Notification") {
            notificationScreen(mainAppNavController, modifier = modifier)
        }
        composable("info") {
            Info(
                mainNavController = mainAppNavController,
                modifier = modifier
            )
        }
    }

}
