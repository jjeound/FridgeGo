package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.data.remote.dto.KakaoAccessTokenRequest
import com.example.untitled_capstone.data.remote.dto.LocationDto
import com.example.untitled_capstone.domain.model.AccountInfo
import com.example.untitled_capstone.domain.model.Address

interface LoginRepository {

    suspend fun kakaoLogin(accessToken: String): Resource<AccountInfo>

    suspend fun setNickname(nickname: String): Resource<String>

    suspend fun getAddressByCoord(x: String, y: String): Resource<Address>

    suspend fun setLocation(district: String, neighborhood: String): Resource<String>

    suspend fun modifyNickname(nickname: String): Resource<String>
}
