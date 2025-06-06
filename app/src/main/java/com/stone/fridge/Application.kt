package com.stone.fridge

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
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
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "유통기한 알림", importance)
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