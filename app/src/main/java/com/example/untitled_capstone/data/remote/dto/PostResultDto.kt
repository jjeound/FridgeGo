package com.example.untitled_capstone.data.remote.dto

data class PostResultDto(
    val content: List<PostDto>,
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
