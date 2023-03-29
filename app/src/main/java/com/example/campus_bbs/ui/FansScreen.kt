package com.example.campus_bbs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.R


@Composable
fun UserItem (
    modifier: Modifier = Modifier,
    //user: User,
    /* TODO: add User data */
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "your avatar",
            modifier = modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Column(
            modifier = modifier.align(Alignment.CenterVertically),
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
                text = "This is an introduction",
                fontWeight = FontWeight.Light,
                fontSize = 10.sp,
                color = Color.Gray

            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FansScreen(
    modifier: Modifier = Modifier,
    mainAppNavController: NavHostController
) {
    var scrollState by remember { mutableStateOf( ScrollState (0) ) }
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
            items(30) {
                UserItem(modifier)
            }
        }
    }
}