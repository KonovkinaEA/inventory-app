package com.example.inventoryapp.data.model

import java.util.UUID

data class InventoryItem(
    val id: String = UUID.randomUUID().toString(),
    val barcode: String = "",
    val code: String = "",
    val number: String = "",
    val auditorium: String = "",
    val type: String = ""
)
