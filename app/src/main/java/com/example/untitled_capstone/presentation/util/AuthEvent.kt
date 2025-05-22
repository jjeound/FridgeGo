package com.example.untitled_capstone.presentation.util

interface  AuthEvent {
    object Logout : AuthEvent
    object Login : AuthEvent
}