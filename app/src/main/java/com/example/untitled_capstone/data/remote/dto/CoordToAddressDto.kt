package com.example.untitled_capstone.data.remote.dto

data class CoordToAddressDto(
    val meta: MetaDto,
    val documents: List<DocumentsDto>? = null
)
