package com.example.untitled_capstone.data.remote.manager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Constants.FCM_CHANNEL_ID
import com.example.untitled_capstone.data.AppDispatchers
import com.example.untitled_capstone.data.Dispatcher
import com.example.untitled_capstone.data.remote.service.FcmApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FCMService() : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "Message data: ${remoteMessage.data}")
        showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(title: String?, message: String?) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(FCM_CHANNEL_ID, "채팅 알림", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, FCM_CHANNEL_ID)
            .setContentTitle(title ?: "FCM")
            .setContentText(message ?: "You have a new message.")
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }
}