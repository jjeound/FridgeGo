package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoordToAddressDto(
    val meta: MetaDto,
    val documents: List<DocumentsDto>? = null
)
