package com.example.untitled_capstone.presentation.util

sealed class AuthEvent {
    object Logout : AuthEvent()
}