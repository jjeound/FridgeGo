package com.example.untitled_capstone.data.remote.dto

import com.example.untitled_capstone.domain.model.TastePreference

data class PreferenceDto(
    val tastePreference: String
){
    fun toTastePreference(): TastePreference{
        return TastePreference(
            tastePreference = tastePreference
        )
    }
}
