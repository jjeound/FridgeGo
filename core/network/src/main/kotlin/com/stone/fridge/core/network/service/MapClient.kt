package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.CoordToAddress
import javax.inject.Inject

class MapClient @Inject constructor(
    private val mapApi: MapApi,
) {
    suspend fun getRegion(
        x: String,
        y: String
    ): CoordToAddress = mapApi.getRegion(x = x, y = y)
}