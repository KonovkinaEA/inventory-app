package com.example.inventoryapp.data.api.model

import com.example.inventoryapp.data.model.InventoryItem

data class UpdatedData(
    val items: List<InventoryItem>,
    val idsToDelete: List<String>
)
