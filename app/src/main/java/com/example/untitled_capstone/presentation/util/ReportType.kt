package com.example.untitled_capstone.presentation.util

enum class ReportType(val value: String) {
    INAPPROPRIATE_LANGUAGE("부적절한 언어"),
    SCAM("악성"),
    HARASSMENT("혐오"),
    OTHER("기타");

    companion object {
        fun fromString(value: String): ReportType? {
            return ReportType.entries.find { it.name == value }
        }
    }
}