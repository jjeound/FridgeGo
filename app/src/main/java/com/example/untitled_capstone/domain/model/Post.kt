package com.example.untitled_capstone.domain.model

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val image: List<String?>,
    val location: String,
    val time: String,
    val totalNumbOfPeople: Int,
    val currentNumOfPeople: Int,
    val likes: Int,
    val isLiked: Boolean,
    val price: Int,
    val category: String,
    val views: Int,
    //val user: User,
    //val level: String
)