package com.stone.fridge.data.remote.dto

data class CoordToAddressDto(
    val meta: MetaDto,
    val documents: List<DocumentsDto>? = null
)
