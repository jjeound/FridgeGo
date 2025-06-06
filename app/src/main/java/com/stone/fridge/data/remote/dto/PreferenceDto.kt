package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.TastePreference

data class PreferenceDto(
    val tastePreference: String
){
    fun toTastePreference(): TastePreference{
        return TastePreference(
            tastePreference = tastePreference
        )
    }
}
