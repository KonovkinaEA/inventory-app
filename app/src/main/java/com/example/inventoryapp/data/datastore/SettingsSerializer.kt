package com.example.inventoryapp.data.datastore

import android.util.Log
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<UserSettings> {
    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        return try {
            Json.decodeFromString(
                deserializer = UserSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Log.e("datastore", "An error occurred while loading data")
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = UserSettings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
