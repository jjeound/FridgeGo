package com.stone.fridge.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DocumentsDto(
    val road_address: RoadAddressDto,
    val address: AddressDto,
)
