package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FridgeResultDto(
    val content: List<ContentDto>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: PageableDto,
    val size: Int,
    val sort: SortDto,
    val totalElements: Long,
    val totalPages: Int
)