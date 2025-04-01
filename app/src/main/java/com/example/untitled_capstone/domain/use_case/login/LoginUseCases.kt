package com.example.untitled_capstone.domain.use_case.login

data class LoginUseCases(
    val kakaoLogin: KakaoLogin,
    val setNickname: SetNickname,
    val getAddressByCoord: GetAddressByCoord,
    val setLocation: SetLocation,
    val modifyNickname: ModifyNickname
)
