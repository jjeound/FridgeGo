package com.example.untitled_capstone.presentation.util

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Constants.NOTIFICATION_CHANNEL_ID
import com.example.untitled_capstone.domain.use_case.notification.InsertNotificationUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ExpirationAlarmWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val insertNotificationUseCase: InsertNotificationUseCase
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val itemName = inputData.getString("name") ?: "알 수 없음"
        val message = inputData.getString("message") ?: "유통기한 알림"
        showNotification(itemName, message)
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val time = System.currentTimeMillis()
        // 알림 생성
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.thumbnail)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(time.toInt(), notification)

        insertNotificationUseCase(
            com.example.untitled_capstone.domain.model.Notification(
                title = title,
                content = message,
                time = convertMillisToDate(time),
                isRead = false
            )
        )
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        return formatter.format(Date(millis))
    }
}

fun scheduleExpirationAlarms(context: Context, itemName: String, expirationTime: Long) {
    val currentTime = System.currentTimeMillis()

    // 알람 시간 계산 (3일 전, 1일 전, 당일)
    val alertTimes = listOf(
        expirationTime - TimeUnit.DAYS.toMillis(3), // 3일 전
        expirationTime - TimeUnit.DAYS.toMillis(1), // 1일 전
        expirationTime                               // 당일
    )

    val alertMessages = listOf(
        "$itemName 의 유통기한이 3일 남았습니다.",
        "$itemName 의 유통기한이 하루 남았습니다.",
        "$itemName 의 유통기한이 오늘까지 입니다."
    )

    val workManager = WorkManager.getInstance(context)

    alertTimes.forEachIndexed { index, time ->
        if (time > currentTime) {
            val delay = time - currentTime

            val inputData = Data.Builder()
                .putString("item_name", itemName)
                .putString("message", alertMessages[index])
                .build()

            val workRequest = OneTimeWorkRequestBuilder<ExpirationAlarmWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(itemName)
                .build()

            workManager.enqueue(workRequest)
        }
    }
}

fun cancelExpirationAlarm(context: Context, itemName: String) {
    val workManager = WorkManager.getInstance(context)

    // 같은 태그를 가진 모든 작업 취소
    workManager.cancelAllWorkByTag(itemName)
}