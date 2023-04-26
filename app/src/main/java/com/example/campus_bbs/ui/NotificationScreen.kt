package com.example.campus_bbs.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.data.MessageInfo
import com.example.campus_bbs.data.User
import com.example.campus_bbs.data.UserMeta
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
//        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item {
            subscribeList()
            Spacer(modifier = Modifier.height(2.dp))
        }

        items(10) {
            MessageItemInScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MessageItemInScreen(
    targetUserMeta: UserMeta = FakeDataGenerator().generateSingleUserMeta(),
    messageInfoList: List<MessageInfo> = FakeDataGenerator().generateFakeMessageInfoList(10),
    modifier: Modifier = Modifier
) {
    Card(
       shape = RectangleShape
    ) {
        Row {
            Spacer(modifier = Modifier.width(5.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(targetUserMeta.userIconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "test image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .height(45.dp)
                    .width(45.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = targetUserMeta.userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Top)
                    )
                }
                if (messageInfoList.isNotEmpty()) {
                    Text(
                        text = messageInfoList.last().messageContent,
                        modifier = Modifier.padding(5.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Divider()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
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