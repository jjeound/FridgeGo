package com.stone.fridge.presentation.util

interface  AuthEvent {
    object Logout : AuthEvent
    object Login : AuthEvent
}