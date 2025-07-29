package com.stone.fridge.data.remote.dto

import com.stone.fridge.domain.model.TastePreference
import kotlinx.serialization.Serializable

@Serializable
data class PreferenceDto(
    val tastePreference: String
){
    fun toTastePreference(): TastePreference{
        return TastePreference(
            tastePreference = tastePreference
        )
    }
}
