package com.example.untitled_capstone.domain.use_case.my

data class MyUseCases(
    val getMyProfile: GetMyProfile,
    val getOtherProfile: GetOtherProfile,
    val getAccessToken: GetAccessToken,
    val logout: Logout,
    val uploadProfileImage: UploadProfileImage,
    val reportUser: ReportUserUseCase,
    val getMyNicknameUseCase: GetMyNicknameUseCase
)
