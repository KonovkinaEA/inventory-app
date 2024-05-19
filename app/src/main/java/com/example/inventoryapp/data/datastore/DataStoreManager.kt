package com.example.inventoryapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private val Context.protoDataStore by dataStore("settings.json", SettingsSerializer)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingsDataStore = appContext.protoDataStore
    val userSettings = settingsDataStore.data

    suspend fun saveUsername(username: String) {
        settingsDataStore.updateData { settings ->
            settings.copy(username = username)
        }
    }
}
