package com.stone.fridge.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Documents(
    @SerialName("road_address")val roadAddress: RoadAddress,
    val address: Address,
)
