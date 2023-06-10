package com.example.campus_bbs.ui.components

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.Blog
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.UserMeta
import com.example.campus_bbs.ui.AppViewModelProvider
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.model.NavControlViewModel
import com.example.campus_bbs.ui.model.UserViewModel
import com.example.campus_bbs.ui.model.VisitingUserHomeViewModel
import com.example.campus_bbs.ui.network.UserApi
import kotlinx.coroutines.launch

@Composable
@Preview
fun UserPanelInBlog(
    userMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    timeString: String = "time"
) {
    val navControlViewModel: NavControlViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val visitingUserHomeViewModel: VisitingUserHomeViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val currentUserState = userViewModel.currentUserState.collectAsState()

    val subscribed = currentUserState.value.followList.filter { it.userId == userMeta.userId }.isNotEmpty()

    val scope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userMeta.userIconUrl)
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
                            userMeta.userId
                        )
                        visitingUserHomeViewModel.updateVisitingUser(resp.getUser())
                        Log.i(
                            "GET USER BY ID",
                            resp
                                .getUser()
                                .toString()
                        )
                        navControlViewModel.userHome = resp.getUser()
                        navControlViewModel.mainNavController.navigate("UserHome")
                    }
                }
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(text = userMeta.userName, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                if (subscribed) {
                    Text(text = "已关注", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
            Text(text = timeString)
        }
    }
}

@Composable
fun UserPanelInComment(
    userMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    createTime : String = "time"
) {
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val navControlViewModel: NavControlViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    val scope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(top = 5.dp, start = 5.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userMeta.userIconUrl)
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
                            userMeta.userId
                        )
                        navControlViewModel.userHome = resp.getUser()
                        navControlViewModel.mainNavController.navigate("UserHome")
                    }
                }
        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(65.dp)
                    .fillMaxWidth()
            ) {
                Column() {
                    Text(
                        text = userMeta.userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(text = createTime)
                }

            }
        }
    }
}