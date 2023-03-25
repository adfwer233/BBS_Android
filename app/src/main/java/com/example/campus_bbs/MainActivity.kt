package com.example.campus_bbs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.ui.BlogScreen
import com.example.campus_bbs.ui.CreateBlogScreen
import com.example.campus_bbs.ui.RecommendationScreen
import com.example.campus_bbs.ui.UserHomeScreen
import com.example.campus_bbs.ui.model.RecommendationViewModel
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

@Preview(showBackground = true)
@Composable
fun App() {
    val mainAppNavController = rememberNavController()
    NavHost(navController = mainAppNavController, startDestination = "AppHome") {
        composable("AppHome") {
            AppHome(mainAppNavController)
        }
        composable("BlogScreen") {
            BlogScreen(mainAppNavController)
        }
        composable("UserHome") {
            UserHomeScreen(mainAppNavController = mainAppNavController)
        }
        composable("CreateBlog") {
            CreateBlogScreen(mainAppNavController = mainAppNavController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHome(
    mainAppNavController: NavHostController
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("homepage", "blogs", "info")

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Campus BBS")}
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
        contentPadding -> MainAppView(mainAppNavController, navController, modifier = Modifier.padding(contentPadding))
    }
}

@Composable
fun MainAppView(
    mainAppNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier
) {

    val recommendationViewModel: RecommendationViewModel = viewModel()

    NavHost(navController = navController, startDestination = "homepage") {
        composable("homepage") {
//            HomePageTest(
//                update = { navController.navigate("blogs") },
//                modifier
//            )
            RecommendationScreen(mainAppNavController, modifier = modifier, recommendationViewModel = recommendationViewModel)
        }
        composable("blogs") {
            Blogs(
                update = { navController.navigate("homepage") },
                modifier
            )
        }
        composable("info") {
            Info(
                update = { mainAppNavController.navigate("UserHome") },
                modifier
            )
        }
        composable("blogsScreen") {
            BlogScreen(mainAppNavController)
        }
    }

}


@Composable
fun Blogs (
    update: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(onClick = update) {
            Text(text = "Test Button in Blogs")
        }
    }
}

@Composable
fun Info (
    update: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(onClick = update) {
            Text(text = "Test Button in Info")
        }
    }
}