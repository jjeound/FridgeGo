package com.stone.fridge.core.common

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.formatLocaleDateTimeToKoreanDateTime(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US) // ISO 8601 기반 예시
    inputFormat.timeZone = TimeZone.getTimeZone("UTC") // 입력이 UTC 기준이라고 가정

    val date: Date = try {
        inputFormat.parse(this)
    } catch (e: Exception) {
        return this // 파싱 실패 시 원본 문자열 반환
    }

    val koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul")
    val koreaCalendar = Calendar.getInstance(koreaTimeZone).apply {
        time = date
    }

    val today = Calendar.getInstance(koreaTimeZone).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val yesterday = Calendar.getInstance(koreaTimeZone).apply {
        time = today.time
        add(Calendar.DAY_OF_YEAR, -1)
    }

    return when {
        isSameDay(koreaCalendar, today) -> {
            val timeFormat = SimpleDateFormat("a h:mm", Locale.KOREAN)
            timeFormat.timeZone = koreaTimeZone
            timeFormat.format(koreaCalendar.time)
        }
        isSameDay(koreaCalendar, yesterday) -> {
            "어제"
        }
        else -> {
            val dateFormat = SimpleDateFormat("M월 d일", Locale.KOREAN)
            dateFormat.timeZone = koreaTimeZone
            dateFormat.format(koreaCalendar.time)
        }
    }
}

fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun String.levelToKor(): String{
    return when(this){
        "BeginnerChef" -> "초보 요리사"
        "HomeCook" -> "집밥러"
        "SousChef" -> "수셰프"
        "HeadChef" -> "헤드셰프"
        "MasterChef" -> "마스터 셰프"
        else -> "초보 요리사"
    }
}