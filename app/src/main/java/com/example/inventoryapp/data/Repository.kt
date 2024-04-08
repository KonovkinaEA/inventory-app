package com.example.inventoryapp.data

import com.example.inventoryapp.data.model.InventoryItem

interface Repository {
    suspend fun getAllItems(): List<InventoryItem>
    suspend fun getItemsInAuditorium(auditorium: String): List<InventoryItem>
    suspend fun getItemById(id: String): InventoryItem?
    suspend fun getItemByBarcode(barcode: String): InventoryItem?
    suspend fun saveItem(item: InventoryItem)
    suspend fun deleteItem(id: String)
}
