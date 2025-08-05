package com.stone.fridge.core.auth

interface  AuthEvent {
    object Logout : AuthEvent
    object Login : AuthEvent
}