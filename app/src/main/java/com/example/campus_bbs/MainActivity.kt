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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun App() {
    val selectedItem by remember { mutableStateOf(0) }
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
                        onClick = { navController.navigate(items[index]) }
                    )
                }
            }
        }
    ) {
        contentPadding -> MainAppView(navController, modifier = Modifier.padding(contentPadding))
    }
}

@Composable
fun MainAppView(
    navController: NavHostController,
    modifier: Modifier
) {

    NavHost(navController = navController, startDestination = "homepage") {
        composable("homepage") {
            HomePageTest(
                update = { navController.navigate("blogs") },
                modifier
            )
        }
        composable("blogs") {
            Blogs(update = { navController.navigate("homepage") }, modifier)
        }
        composable("info") {
            Info(update = {navController.navigate("homepage")}, modifier)
        }
        /*...*/
    }

//    Column(
//        modifier = modifier
//    ) {
//        Text(text = "test main text")
//        Button(onClick = { }) {
//            Text(text = "Button")
//        }
//    }
}

@Composable
fun HomePageTest (
    update: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Button(onClick = update) {
            Text(text = "Test Button")
        }
        Text(text = "Homepage")
        Text(text = "Homepage")
        Text(text = "Homepage")
        Text(text = "Homepage")
        Text(text = "Homepage")
        Text(text = "Homepage")
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