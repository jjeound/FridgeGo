package com.stone.fridge.core.model

import kotlinx.serialization.Serializable

@Serializable
data class KakaoAccessTokenRequest(val accessToken: String)