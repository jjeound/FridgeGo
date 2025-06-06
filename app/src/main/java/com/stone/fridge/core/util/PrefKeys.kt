package com.stone.fridge.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    val NICKNAME = stringPreferencesKey("nickname")
    val APP_ENTRY = booleanPreferencesKey("app_entry")
    val MY_PROFILE = stringPreferencesKey("my_profile")
    val DONG = stringPreferencesKey("dong")
    val IS_UNREAD_NOTIFICATION = booleanPreferencesKey("is_unread_notification")
}