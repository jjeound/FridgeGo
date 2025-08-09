package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.CoordToAddress
import com.stone.fridge.core.network.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface MapApi {
    @GET("/v2/local/geo/coord2address.json")
    @Headers("Content-Type: application/json")
    suspend fun getRegion(
        @Header("Authorization") apiKey: String = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
        @Query("x") x: String,
        @Query("y") y: String
    ): CoordToAddress
}