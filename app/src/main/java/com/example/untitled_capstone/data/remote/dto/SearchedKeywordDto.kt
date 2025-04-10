package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.Keyword

data class SearchedKeywordDto(
    val keyword: String
){
    fun toKeyword(): Keyword {
        return Keyword(
            keyword = keyword
        )
    }
}
