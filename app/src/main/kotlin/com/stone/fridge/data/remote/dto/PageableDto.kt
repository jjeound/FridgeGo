package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageableDto(
    val offset: Long,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: SortDto,
    val unpaged: Boolean
)