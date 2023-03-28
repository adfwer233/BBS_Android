package com.example.campus_bbs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.User
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.MainViewModel

@Composable
fun notificationScreen(
    mainAppNavController: NavHostController,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            subscribeList()
            Spacer(modifier = Modifier.height(2.dp))
        }

        items(mainViewModel.notificationViewModel.uiState.value.blogListOfSubscribedUsers) {
            BlogsCard(
                moreButtonOnClick = {
                    mainViewModel.BlogViewModel.updateBlog(it)
                    mainAppNavController.navigate("BlogScreen")
                },
                blog = it
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun subscribeList (
    subscribeUserList: List<User> = FakeDataGenerator().generateUserList(10),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column {
            Text(text = "Subscribed Users", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            LazyRow (
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                item {
                    Spacer(Modifier.width(3.dp))
                }
                items (subscribeUserList) {
                    Column {
                        Image(
                            imageVector = Icons.Filled.Face,
                            contentDescription = "",
                            modifier=Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(text = it.userName)
                    }
                }
            }
        }
    }
}