package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.TastePreference

data class PreferenceResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PreferenceDto
){
    fun toTastePreference(): TastePreference{
        return TastePreference(
            tastePreference = result.tastePreference
        )
    }
}
