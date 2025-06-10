package com.stone.fridge

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.stone.fridge.core.util.Constants.NOTIFICATION_CHANNEL_ID
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.stone.fridge.core.util.Constants.FCM_CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application: Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
        })
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "소비기한 알림", importance)
        val chatChannel = NotificationChannel(
            FCM_CHANNEL_ID,
            "채팅 알림",
            importance
        )
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(chatChannel)
    }
}