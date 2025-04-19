package com.example.untitled_capstone.presentation.util

import com.example.untitled_capstone.presentation.feature.post.composable.Category

enum class ReportType(val value: String) {
    INAPPROPRIATE_LANGUAGE("부적절한 언어"),
    SCAM("악성"),
    HARASSMENT("혐오"),
    OTHER("기타");

    companion object {
        fun fromKor(value: String): String? {
            return ReportType.entries.find { it.value == value }?.name
        }
    }
}