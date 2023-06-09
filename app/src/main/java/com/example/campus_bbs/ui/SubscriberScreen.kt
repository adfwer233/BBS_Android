package com.example.campus_bbs.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.R
import com.example.campus_bbs.data.User
import com.example.campus_bbs.data.UserMeta
import com.example.campus_bbs.ui.components.BlogsCard
import com.example.campus_bbs.ui.model.FanSubScreenViewModel
import com.example.campus_bbs.ui.model.NavControlViewModel
import com.example.campus_bbs.ui.model.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriberScreen(
    modifier: Modifier = Modifier,
    mainAppNavController: NavHostController
) {
    var fanSubScreenViewModel : FanSubScreenViewModel = viewModel(LocalContext.current as ComponentActivity)
    val userViewModel: UserViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text( text = "Fans") },
                navigationIcon = {
                    Button(onClick = { mainAppNavController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(modifier = Modifier.padding(contentPadding), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            items(fanSubScreenViewModel.user.subscriberList) {
                UserItem(user = it)
            }
        }
    }
}
