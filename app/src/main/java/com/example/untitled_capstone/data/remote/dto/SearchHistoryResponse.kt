package com.example.untitled_capstone.data.remote.dto

data class SearchHistoryResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<SearchedKeywordDto>? = null
)
