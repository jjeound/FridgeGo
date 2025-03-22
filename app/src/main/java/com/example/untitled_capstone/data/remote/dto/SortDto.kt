package com.example.untitled_capstone.data.remote.dto

data class SortDto(
    val direction: String,
    val nullHandling: String,
    val ascending: Boolean,
    val property: String,
    val ignoreCase: Boolean
)