package com.example.inventoryapp.data.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val username: String? = null
)
