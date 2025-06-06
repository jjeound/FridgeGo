package com.stone.fridge.data.repository

import android.app.Application
import androidx.datastore.preferences.core.edit
import com.stone.fridge.core.util.PrefKeys.APP_ENTRY
import com.stone.fridge.domain.repository.LocalUserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalUserRepositoryImpl @Inject constructor(
    private val application: Application
) : LocalUserRepository {

    override suspend fun saveAppEntry() {
        application.dataStore.edit { settings ->
            settings[APP_ENTRY] = true
        }
    }

    override suspend fun readAppEntry(): Boolean {
        return application.dataStore.data.map { preferences ->
            preferences[APP_ENTRY] == true
        }.first()
    }
}