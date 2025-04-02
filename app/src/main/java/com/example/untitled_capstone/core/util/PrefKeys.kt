package com.example.untitled_capstone.core.util

import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    val NICKNAME = stringPreferencesKey("nickname")
    val EMAIL = stringPreferencesKey("email")
    val IMAGE_URL = stringPreferencesKey("image_url")
}