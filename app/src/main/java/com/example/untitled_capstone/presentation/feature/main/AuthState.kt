package com.example.untitled_capstone.presentation.feature.main

sealed class AuthState{
    object Loading : AuthState()         // 로딩 중
    object Success : AuthState()         // 성공적으로 완료됨
    data class Error(val message: String) : AuthState() // 에러 발생
    object Logout : AuthState()          // 로그아웃 상태
}
