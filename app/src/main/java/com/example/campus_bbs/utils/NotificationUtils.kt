package com.example.campus_bbs.utils

import android.annotation.SuppressLint
import android.app.Notification.VISIBILITY_PUBLIC
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.campus_bbs.MainActivity
import com.example.campus_bbs.R
import okhttp3.internal.notify
import java.util.Date
import kotlin.random.Random


@RequiresApi(Build.VERSION_CODES.O)



class WaterNotificationService(
    private val context: Context
)

@SuppressLint("IntentReset", "UnspecifiedImmutableFlag")
@RequiresApi(Build.VERSION_CODES.O)
fun showBasicNotification(context: Context, name: String, content: String, chatIndex: Int) {

    val channelId = "water_notification" + Random.nextInt()

    val notificationChannel = NotificationChannel(
        channelId, // id
        "Water", // name
        NotificationManager.IMPORTANCE_HIGH // importance
    )

    val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(notificationChannel)

    val activityIntent= Intent(context, MainActivity::class.java)
    Log.e("Create Intent", activityIntent.getStringExtra("data").toString())
    activityIntent.putExtra("data", "CommunicationScreen/?index="+chatIndex)
    Log.e("Create Intent", activityIntent.getStringExtra("data").toString())
    val pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.avatar)
        .setContentTitle(name)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .setStyle(NotificationCompat.BigTextStyle())
        .setContentIntent(pendingIntent)
        .build()

    with(NotificationManagerCompat.from(context)) {
        notificationManager.notify(Random.nextInt(), notification)
    }
}