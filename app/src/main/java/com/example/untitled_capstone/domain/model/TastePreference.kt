package com.example.untitled_capstone.domain.model

import com.example.untitled_capstone.data.remote.dto.PreferenceDto

data class TastePreference(
    val tastePreference: String
){
    fun toPreferenceDto(): PreferenceDto{
        return PreferenceDto(
            tastePreference = tastePreference
        )
    }
}
