package com.example.inventoryapp.data

import com.example.inventoryapp.data.model.InventoryItem

interface Repository {
    fun getAllItems(): List<InventoryItem>
    fun getItemsInAuditorium(auditorium: String): List<InventoryItem>
    fun getItemById(id: String): InventoryItem?
    fun getItemByBarcode(barcode: String): InventoryItem?
    fun saveItem(item: InventoryItem)
    fun deleteItem(id: String)
}
