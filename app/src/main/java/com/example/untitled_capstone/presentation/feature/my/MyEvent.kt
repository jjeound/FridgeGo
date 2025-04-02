package com.example.untitled_capstone.presentation.feature.my

import java.io.File


sealed interface MyEvent{
    data object Logout: MyEvent
    data object GetMyProfile: MyEvent
    data class UploadProfileImage(val file: File): MyEvent
}