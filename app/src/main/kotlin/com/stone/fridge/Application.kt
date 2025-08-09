package com.stone.fridge

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk
import com.stone.fridge.core.common.Constants.FCM_CHANNEL_ID
import com.stone.fridge.core.common.Constants.NOTIFICATION_CHANNEL_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application: Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        KakaoMapSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        fetchFcmTokenSafe()
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

private fun isRunningBaselineProfile(): Boolean {
    // androidx.benchmark 관련 VM Name이 잡히면 BaselineProfile 실행 중
    val vmName = System.getProperty("java.vm.name") ?: ""
    return "androidx.benchmark" in vmName
}

private fun fetchFcmTokenSafe() {
    if (isRunningBaselineProfile()) {
        Log.i("FCM", "Skipping FCM token fetch during BaselineProfile run")
        return
    }

    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("FCM", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }
    })
}