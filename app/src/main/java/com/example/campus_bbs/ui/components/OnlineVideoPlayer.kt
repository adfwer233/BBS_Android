package com.example.campus_bbs.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun OnlineVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier,
    showDelete: Boolean = false,
    deleteOnClick: ()->Unit = {}
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        val context = LocalContext.current
        val exoPlayer = ExoPlayer.Builder(context)
            .build()
            .apply {
                playWhenReady = false
            }

        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
//        exoPlayer.play()

        Box() {

            AndroidView(
                factory = { context ->

                    PlayerView(context).apply {
                        useController = true
                        this.player = exoPlayer
                    }
                },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                update = {
                    it.player?.setMediaItem(MediaItem.fromUri(videoUrl))
                    it.player?.prepare()
                }
            )

            IconButton(
                onClick = {
                            exoPlayer.clearMediaItems()
                            exoPlayer.setMediaItem(mediaItem)
                            exoPlayer.prepare()
                          },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.LightGray, shape = CircleShape)
                    .width(35.dp)
                    .height(35.dp)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "test")
            }

            if (showDelete) {
                IconButton(
                    onClick = deleteOnClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(Color.LightGray, shape = CircleShape)
                        .width(35.dp)
                        .height(35.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "test")
                }
            }
        }
    }

}