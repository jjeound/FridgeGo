package com.example.untitled_capstone.data.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.untitled_capstone.domain.model.FridgeItem
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

@ProvidedTypeConverter
class Converters @Inject constructor(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromFridgeItemsJson(json: String): List<FridgeItem>{
        return jsonParser.fromJson<ArrayList<FridgeItem>>(
            json,
            object : TypeToken<ArrayList<FridgeItem>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toFridgeItemsJson(fridgeItems: List<FridgeItem>): String{
        return jsonParser.toJson(
            fridgeItems,
            object : TypeToken<ArrayList<FridgeItem>>(){}.type
        ) ?: "[]"
    }
}