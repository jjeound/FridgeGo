package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Pageable(
    val offset: Long,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)