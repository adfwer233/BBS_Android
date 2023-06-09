package com.example.campus_bbs

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.campus_bbs.data.User
import com.example.campus_bbs.ui.*
import com.example.campus_bbs.ui.components.*
import com.example.campus_bbs.ui.model.*
import com.example.campus_bbs.ui.network.LoginApi
import com.example.campus_bbs.ui.theme.Campus_BBSTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var route: String = intent.getStringExtra("data") ?: ""
        println(intent)

        intent?.let {
            val uri: Uri? = it.data
            uri?.let {
                uri.path?.let { it1 -> Log.e("route log", it1) }
//                Log.e("routelog)
                uri.getQueryParameter("route")?.let {
                    input -> route = input
                    Log.e("routelog", uri.query.toString())
                    Log.e("routelog", route)
                }
            }
        }
        setContent {
            Campus_BBSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(route)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }
}

@Composable
fun App(
    intentRoutePath: String = ""
) {
    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val tokenState = loginViewModel.tokenFlow.collectAsState(loginViewModel.jwtToken)

    val scope = rememberCoroutineScope()

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                Log.e("asdfasdf", "START")
                scope.launch {
                    notificationViewModel.connect()
                }
            } else if (event == Lifecycle.Event.ON_STOP) {
                Log.e("asdfasdf", "STOP")
                scope.launch {
                    notificationViewModel.websocketManager?.close()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Log.e("asdfasdfsad", intentRoutePath)
    val mainAppNavController = rememberNavController()

    val navControlViewModel: NavControlViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    navControlViewModel.mainNavController = mainAppNavController

    val cameraViewModel = CameraViewModel()

    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)


    val recommendationViewModel: RecommendationViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val userState = userViewModel.currentUserState.collectAsState()


    NavHost(navController = mainAppNavController, startDestination = "AppHome") {
        composable("AppHome") {
//            OnlineVideoPlayer(videoUrl = "https://cloud.tsinghua.edu.cn/f/d059ce302d864d7ab9ee/?dl=1",)
//            SimpleCameraScreen(cameraViewModel = cameraViewModel)
//            ImagePicker()
//            AppHome(mainAppNavController)
//            UserHomeScreen()
            if (tokenState.value == "") {
                LoginScreen()
            }
            else {
                userViewModel.getCurrentUser()
                AppHome(mainAppNavController)
            }
//            CommunicationScreen()
        }
        composable(
            route = "BlogScreen/?id={id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            BlogScreen(mainAppNavController, postId = it.arguments?.getString("id"))
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
        composable("subscriberScreen") {
            SubscriberScreen(mainAppNavController = mainAppNavController)
        }
        composable(
            route = "CommunicationScreen/?index={index}",
            arguments = listOf(
                navArgument("index") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            CommunicationScreen(mainNavController = mainAppNavController, targetUserId = it.arguments?.getString("index"))
        }
        composable(
            route = "EditProfile",
        ) {
            EditProfileScreen()
        }
        composable("Search") {
            SearchScreen()
        }
    }

    if (intentRoutePath != "") {
        scope.launch {
            notificationViewModel.updateUserChat()
            recommendationViewModel.updateBlogList(loginViewModel.jwtToken)
            delay(1000)
            mainAppNavController.navigate(intentRoutePath)
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
    val notificationViewModel: NotificationViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    notificationViewModel.updateUserChat()
    notificationViewModel.registeredContext = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Campus BBS") },
                actions = {
                    IconButton(onClick = {
                        mainAppNavController.navigate("Search")
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "User Icon")
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("log out") },
                                onClick = {
                                    scope.launch {
                                        LoginApi.retrofitService.logout(loginViewModel.jwtToken)
                                        loginViewModel.setToken("")
                                    }
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                                Icon(Icons.Filled.Favorite, contentDescription = item)
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

    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

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
        composable(
            route = "Notification/?index={index}",
            arguments = listOf(
                navArgument("index") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            it.arguments?.getInt("index")
                ?.let { it1 -> notificationScreen(mainAppNavController, modifier = modifier, index = it1) }
        }
        composable(
            route = "Notification",
        ) {
            notificationScreen(mainAppNavController, modifier = modifier)
        }
        composable("info") {
            userViewModel.getCurrentUser()
            Info(
                mainNavController = mainAppNavController,
                modifier = modifier
            )
        }
    }

}
