package com.example.campus_bbs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.leanback.widget.Row
import com.example.campus_bbs.data.BlogComment
import com.example.campus_bbs.data.FakeDataGenerator
import com.example.campus_bbs.ui.FollowingComment
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CommentSheet(
    comment: BlogComment = FakeDataGenerator().generateSingleComment(2),
    modifier: Modifier = Modifier
) {


    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        LazyColumn(
            modifier = Modifier
        ) {

            item {
                Column {
                    UserPanelInComment(userMeta = comment.creator)

                    Spacer(modifier = Modifier.height(3.dp))

                    Box(modifier = Modifier.padding(5.dp)) {
                        androidx.compose.material3.Text(text = comment.commentContent)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(5.dp))
            }

            items(comment.followingComment) {
                FollowingComment(comment = it)
            }
        }
    }
}