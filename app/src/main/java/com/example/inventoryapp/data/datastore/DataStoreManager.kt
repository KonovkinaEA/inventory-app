package com.example.inventoryapp.data.datastore

import android.content.Context
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.protoDataStore by dataStore("settings.json", SettingsSerializer)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingsDataStore = appContext.protoDataStore
    val userSettings = settingsDataStore.data

    suspend fun saveIpAddress(address: String) {
        settingsDataStore.updateData { settings ->
            settings.copy(ipAddress = address)
        }
    }

    suspend fun getIpAddress(): String? {
        val settings = userSettings.first()
        return settings.ipAddress
    }
}
