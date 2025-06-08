package com.stone.fridge.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    val NICKNAME = stringPreferencesKey("nickname")
    val USER_ID = longPreferencesKey("user_id")
    val APP_ENTRY = booleanPreferencesKey("app_entry")
    val DONG = stringPreferencesKey("dong")
    val IS_UNREAD_NOTIFICATION = booleanPreferencesKey("is_unread_notification")
    val FCM_OLD_TOKEN = stringPreferencesKey("fcm_old_token")
    val FCM_NEW_TOKEN = stringPreferencesKey("fcm_new_token")
}