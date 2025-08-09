package com.stone.fridge.core.navigation

import android.net.Uri
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import com.stone.fridge.core.model.Post
import kotlinx.serialization.json.Json

object PostNavType : NavType<Post?>(isNullableAllowed = true) {

    override fun put(bundle: Bundle, key: String, value: Post?) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): Post? =
        BundleCompat.getParcelable(bundle, key, Post::class.java)

    override fun parseValue(value: String): Post {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Post?): String = Uri.encode(Json.encodeToString(value))
}