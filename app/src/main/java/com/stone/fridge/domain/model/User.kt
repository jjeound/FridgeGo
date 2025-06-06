package com.stone.fridge.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val createdAt: String,
    val email: String,
    val exp: Int,
    val id: Int,
    val ingredientList: List<Ingredient>,
    val name: String,
    val nickname: String,
    val profileImage: String?,
    val updatedAt: String
)
