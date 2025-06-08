package com.stone.fridge.data.remote.manager

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.datastore.preferences.core.edit
import com.stone.fridge.R
import com.stone.fridge.core.util.Constants.FCM_CHANNEL_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.stone.fridge.core.util.Constants.NOTIFICATION_CHANNEL_ID
import com.stone.fridge.core.util.PrefKeys.FCM_NEW_TOKEN
import com.stone.fridge.data.repository.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FCMService() : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val roomId = remoteMessage.data["roomId"]?.toLongOrNull()
        Log.d("FCM", "Message data: ${remoteMessage.data}")
        showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body, roomId)
    }

    @SuppressLint("ServiceCast")
    private fun showNotification(title: String?, message: String?, roomId: Long?) {
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        val notification = if(roomId != null){
            val activityIntent = Intent(Intent.ACTION_VIEW, "deeplink://chat/$roomId".toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            NotificationCompat.Builder(this, FCM_CHANNEL_ID)
                .setContentTitle(title ?: "냉장GO")
                .setContentText(message ?: "새로운 메시지가 도착했습니다.")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.thumbnail)
                .setAutoCancel(true)
                .build()
        } else {
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title ?: "냉장GO")
                .setContentText(message ?: "새로운 알림이 도착했습니다.")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.thumbnail)
                .setAutoCancel(true)
                .build()
        }

        notificationManager.notify(0, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val context = applicationContext
        saveFcmToken(token, context)
    }
    
    private fun saveFcmToken(token: String, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs[FCM_NEW_TOKEN] = token
            }
            Log.d("FCM", "New token saved: $token")
        }
    }
}