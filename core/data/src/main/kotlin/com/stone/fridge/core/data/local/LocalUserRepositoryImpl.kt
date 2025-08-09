package com.stone.fridge.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.data.util.PrefKeys.APP_ENTRY
import com.stone.fridge.core.data.util.PrefKeys.DONG
import com.stone.fridge.core.data.util.PrefKeys.NICKNAME
import com.stone.fridge.core.data.util.PrefKeys.USER_ID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalUserRepository {

    override suspend fun saveAppEntry() {
        dataStore.edit { settings ->
            settings[APP_ENTRY] = true
        }
    }

    override suspend fun readAppEntry(): Boolean {
        return dataStore.data.map { prefs ->
            prefs[APP_ENTRY] == true
        }.first()
    }

    override suspend fun getNickname(): String? {
        return dataStore.data.map { prefs ->
            prefs[NICKNAME]
        }.first()
    }

    override suspend fun getUserId(): Long? {
        return dataStore.data.map { prefs ->
            prefs[USER_ID]
        }.first()
    }

    override suspend fun getLocation(): String? {
        return dataStore.data.map { prefs ->
            prefs[DONG]
        }.first()
    }
}