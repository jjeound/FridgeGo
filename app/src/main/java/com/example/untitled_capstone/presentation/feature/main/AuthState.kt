package com.example.untitled_capstone.presentation.feature.main

sealed class AuthState{
    object Idle : AuthState()         // inital state
    object Login : AuthState()         // 성공적으로 완료됨
    object Logout : AuthState()          // 로그아웃 상태
}
