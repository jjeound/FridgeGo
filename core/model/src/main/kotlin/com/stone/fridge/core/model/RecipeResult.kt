package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class RecipeResult(
    val content: List<RecipeRaw>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort,
    val totalElements: Long,
    val totalPages: Int
)
