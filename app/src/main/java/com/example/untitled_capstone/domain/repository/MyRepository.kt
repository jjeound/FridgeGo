package com.example.untitled_capstone.domain.repository

import com.example.untitled_capstone.core.util.Resource
import com.example.untitled_capstone.data.remote.dto.ApiResponse
import com.example.untitled_capstone.domain.model.Profile

interface MyRepository {
    suspend fun logout(): Resource<ApiResponse>

    suspend fun getMyProfile(): Resource<Profile>

    suspend fun getOtherProfile(nickname: String): Resource<Profile>
}