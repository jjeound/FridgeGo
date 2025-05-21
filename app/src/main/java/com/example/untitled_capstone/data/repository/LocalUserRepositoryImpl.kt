package com.example.untitled_capstone.data.repository

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.untitled_capstone.core.util.Constants
import com.example.untitled_capstone.core.util.PrefKeys.APP_ENTRY
import com.example.untitled_capstone.domain.repository.LocalUserRepository
import kotlinx.coroutines.flow.Flow
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

    override fun readAppEntry(): Flow<Boolean> {
        return application.dataStore.data.map { preferences ->
            preferences[APP_ENTRY] == true
        }
    }
}