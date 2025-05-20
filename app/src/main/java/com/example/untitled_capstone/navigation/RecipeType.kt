package com.example.untitled_capstone.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.example.untitled_capstone.domain.model.Recipe
import kotlinx.serialization.json.Json


val RecipeType = object : NavType<Recipe>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Recipe? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): Recipe {
        return Json.decodeFromString(value)
    }

    override fun put(bundle: Bundle, key: String, value: Recipe) {
        bundle.putString(key, Json.encodeToString(Recipe.serializer(), value))
    }

    override fun serializeAsValue(value: Recipe): String {
        return Json.encodeToString(Recipe.serializer(), value)
    }
}