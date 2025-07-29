package com.stone.fridge.domain.model

import com.stone.fridge.data.remote.dto.PreferenceDto

data class TastePreference(
    val tastePreference: String
){
    fun toPreferenceDto(): PreferenceDto{
        return PreferenceDto(
            tastePreference = tastePreference
        )
    }
}
