package com.example.untitled_capstone.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.untitled_capstone.domain.model.ChattingRoom
import com.example.untitled_capstone.domain.model.FridgeItem
import com.example.untitled_capstone.domain.model.Post
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

inline fun <reified T : Any> createNavType(): NavType<T> = object : NavType<T>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, Json.encodeToString(value))
    }
}

object CustomNavType {
    val ChatType by lazy { createNavType<ChattingRoom>() }
}