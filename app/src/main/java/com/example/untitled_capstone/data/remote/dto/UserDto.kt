package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.User

data class UserDto(
    val createdAt: String,
    val email: String,
    val exp: Int,
    val id: Int,
    val ingredientList: List<IngredientDto>,
    val name: String,
    val nickname: String,
    val profileImage: String,
    val updatedAt: String
){
    fun toUser(): User{
        return User(
            createdAt = createdAt,
            email = email,
            exp = exp,
            id = id,
            ingredientList = ingredientList.map { it.toIngredient() },
            name = name,
            nickname = nickname,
            profileImage = profileImage,
            updatedAt = updatedAt
        )
    }
}