package com.example.inventoryapp.data

import com.example.inventoryapp.data.model.InventoryItem

interface Repository {
    suspend fun getItems(): List<InventoryItem>
    suspend fun getItemsByLocation(location: String): List<InventoryItem>
    suspend fun getItemById(id: String): InventoryItem?
    suspend fun getItemByBarcode(barcode: String): InventoryItem?
    suspend fun getItemByCode(code: String): InventoryItem?
    suspend fun getItemByInventoryNum(inventoryNum: String): InventoryItem?
    suspend fun saveItem(item: InventoryItem)
    suspend fun deleteItem(id: String)
    suspend fun downloadItemsExcel()
}
