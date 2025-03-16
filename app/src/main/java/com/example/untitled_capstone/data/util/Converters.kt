package com.example.untitled_capstone.data.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.untitled_capstone.domain.model.FridgeItem
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<FridgeItem>{
        return jsonParser.fromJson<ArrayList<FridgeItem>>(
            json,
            object : TypeToken<ArrayList<FridgeItem>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(fridgeItem: List<FridgeItem>): String{
        return jsonParser.toJson(
            fridgeItem,
            object : TypeToken<ArrayList<FridgeItem>>(){}.type
        ) ?: "[]"
    }
}