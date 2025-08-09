package com.stone.fridge.core.network.service

import com.stone.fridge.core.model.Location
import com.stone.fridge.core.model.Profile
import com.stone.fridge.core.model.Report
import com.stone.fridge.core.network.model.ApiResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class MyClient @Inject constructor(
    private val myApi: MyApi,
) {
    suspend fun getProfile(): ApiResponse<Profile> = myApi.getProfile()

    suspend fun getOtherProfile(nickname: String): ApiResponse<Profile> = myApi.getOtherProfile(nickname)

    suspend fun logout(): ApiResponse<String> = myApi.logout()

    suspend fun getLocation(): ApiResponse<Location> = myApi.getLocation()

    suspend fun uploadProfileImage(profileImage: MultipartBody.Part): ApiResponse<String> =
        myApi.uploadProfileImage(profileImage)

    suspend fun reportUser(targetUserId: Long, report: Report): ApiResponse<String> =
        myApi.reportUser(targetUserId, report)

    suspend fun deleteProfileImage(): ApiResponse<String> = myApi.deleteProfileImage()

    suspend fun modifyNickname(nickname: String): ApiResponse<String> = myApi.modifyNickname(nickname)

    suspend fun deleteUser(): ApiResponse<String> = myApi.deleteUser()
}