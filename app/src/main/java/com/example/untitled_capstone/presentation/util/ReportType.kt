package com.example.untitled_capstone.presentation.util

enum class ReportType(val value: String) {
    INAPPROPRIATE_LANGUAGE("욕설"),
    SCAM("사기"),
    HARASSMENT("혐오"),
    OTHER("기타");

    companion object {
        fun fromKor(value: String): String? {
            return ReportType.entries.find { it.value == value }?.name
        }
    }
}