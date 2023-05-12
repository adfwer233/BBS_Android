package com.example.campus_bbs

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.ui.DefaultRecommendation
import com.example.campus_bbs.ui.HotRecommendation
import com.example.campus_bbs.ui.SubscribedRecommendation
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
fun BlogsList(mainAppNavController: NavHostController, blogList: List<Int>, modifier: Modifier = Modifier){
    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item { BlogsCard(moreButtonOnClick = { /*TODO*/ }) }
        item { BlogsCard(moreButtonOnClick = { /*TODO*/ }) }
        item { BlogsCard(moreButtonOnClick = { /*TODO*/ }) }
        item { BlogsCard(moreButtonOnClick = { /*TODO*/ }) }
    }
}
@Composable
fun historyScreen(mainAppNavController: NavHostController, navController: NavHostController, modifier: Modifier = Modifier){

    NavHost(navController = navController, startDestination = "Liked"){
        composable("Liked") {
            BlogsList(mainAppNavController = mainAppNavController, blogList = listOf(1), modifier = modifier)
        }
        composable("Bookmark") {
            BlogsList(mainAppNavController = mainAppNavController, blogList = listOf(1), modifier = modifier)
        }
        composable("History") {
            BlogsList(mainAppNavController = mainAppNavController, blogList = listOf(1), modifier = modifier)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Info (
    mainNavController: NavHostController,
    modifier: Modifier = Modifier
) {
//    var scrollState by remember { mutableStateOf( ScrollState(0) ) }
    Column(
        modifier = modifier.fillMaxHeight(),
//            .verticalScroll(state = scrollState),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = "your avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier.align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Joker",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier,
                    text = "id: 1145141919810",
                    fontWeight = FontWeight.Light,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            Button(onClick = { /*TODO: Edit Profile*/ }) {
                Text(text = "Edit Profile")
            }
        }

        Row(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clickable { mainNavController.navigate("fansScreen") },
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.Blue,
                    text = "114",
                    fontSize = 15.sp
                )
                Text(
                    text = "关注",
                    fontSize = 15.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    color = Color.Blue,
                    text = "514",
                    fontSize = 15.sp
                )
                Text(
                    text = "粉丝",
                    fontSize = 15.sp
                )
            }
        }


        val items = listOf("Liked", "Bookmark", "History")

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
                    0 -> BlogsList(mainNavController, blogList = listOf(1))
                    1 -> BlogsList(mainNavController, blogList = listOf(1))
                    2 -> BlogsList(mainNavController, blogList = listOf(1))
                }
            }
        }
    }
}