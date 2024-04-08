package com.example.inventoryapp.data.model

import java.util.UUID

data class InventoryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val barcode: String = "",
    val code: String = "",
    val number: String = "",
    val auditorium: String = "",
    val type: ItemType = ItemType.DEFAULT
)
