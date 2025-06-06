package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.Keyword

data class SearchedKeywordDto(
    val keyword: String
){
    fun toKeyword(): Keyword {
        return Keyword(
            keyword = keyword
        )
    }
}
