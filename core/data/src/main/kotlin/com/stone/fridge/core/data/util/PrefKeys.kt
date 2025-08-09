package com.stone.fridge.core.data.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val NICKNAME = stringPreferencesKey("nickname")
    val USER_ID = longPreferencesKey("user_id")
    val APP_ENTRY = booleanPreferencesKey("app_entry")
    val DONG = stringPreferencesKey("dong")
    val IS_UNREAD_NOTIFICATION = booleanPreferencesKey("is_unread_notification")
    val FCM_OLD_TOKEN = stringPreferencesKey("fcm_old_token")
    val FCM_NEW_TOKEN = stringPreferencesKey("fcm_new_token")
    val IS_FIRST_SELECTION = booleanPreferencesKey("is_first_selection")
    val TASTE_PREFERENCE = stringPreferencesKey("taste_preference")
}