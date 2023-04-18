package com.example.campus_bbs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.*
import com.example.campus_bbs.ui.components.CommentSheet
import com.example.campus_bbs.ui.components.FullScreenImageRoller
import com.example.campus_bbs.ui.components.OnlineVideoPlayer
import com.example.campus_bbs.ui.model.MainViewModel
import com.example.campus_bbs.ui.theme.Campus_BBSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Campus_BBSTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val mainAppNavController = rememberNavController()

    val mainViewModel = MainViewModel()
    mainViewModel.recommendationViewModel = viewModel()
    mainViewModel.BlogViewModel = viewModel()
    mainViewModel.notificationViewModel = viewModel()
    mainViewModel.createBlogViewModel = viewModel()

    NavHost(navController = mainAppNavController, startDestination = "AppHome") {
        composable("AppHome") {
//            OnlineVideoPlayer(videoUrl = "https://cloud.tsinghua.edu.cn/f/d059ce302d864d7ab9ee/?dl=1",)

            AppHome(mainAppNavController, mainViewModel)
        }
        composable("BlogScreen") {
            BlogScreen(mainAppNavController, blogViewModel = mainViewModel.BlogViewModel)
        }
        composable("UserHome") {
            UserHomeScreen(mainAppNavController = mainAppNavController)
        }
        composable("CreateBlog") {
            CreateBlogScreen(mainAppNavController = mainAppNavController, mainViewModel = mainViewModel)
        }
        composable("fansScreen") {
            FansScreen(mainAppNavController = mainAppNavController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHome(
    mainAppNavController: NavHostController,
    mainViewModel: MainViewModel
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("homepage", "Notification", "info")

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Campus BBS")},
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "User Icon")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            navController.navigate(items[index]){ popUpTo(items[selectedItem]){inclusive = true} }
                            selectedItem = index
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mainAppNavController.navigate("CreateBlog") }) {
                Text(text = "+", color=Color.White, fontSize = 26.sp)
            }
        }
    ) {
        contentPadding -> MainAppView(mainAppNavController, navController, mainViewModel, modifier = Modifier.padding(contentPadding))
    }
}

@Composable
fun MainAppView(
    mainAppNavController: NavHostController,
    navController: NavHostController,
    mainViewModel: MainViewModel,
    modifier: Modifier
) {

    NavHost(navController = navController, startDestination = "homepage") {
        composable("homepage") {
//            HomePageTest(
//                update = { navController.navigate("blogs") },
//                modifier
//            )
            RecommendationScreen(mainAppNavController, modifier = modifier, mainViewModel = mainViewModel)
        }
        composable("Notification") {
            notificationScreen(mainAppNavController, mainViewModel, modifier = modifier)
        }
        composable("info") {
            Info(
                mainNavController = mainAppNavController,
                modifier = modifier
            )
        }
        composable("blogsScreen") {
            BlogScreen(mainAppNavController)
        }
    }

}
