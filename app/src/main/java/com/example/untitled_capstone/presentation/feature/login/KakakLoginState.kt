package com.example.untitled_capstone.presentation.feature.login

import androidx.paging.PagingData
import com.example.untitled_capstone.data.remote.dto.KakaoLoginResponse
import com.example.untitled_capstone.domain.model.FridgeItem
import kotlinx.coroutines.flow.Flow

data class KakakLoginState(
    val response: KakaoLoginResponse? = null,
    val loading: Boolean = false,
    val error: String = ""
)