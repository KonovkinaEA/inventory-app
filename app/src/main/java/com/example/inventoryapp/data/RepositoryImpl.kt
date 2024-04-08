package com.example.inventoryapp.data

import com.example.inventoryapp.data.db.ItemDao
import com.example.inventoryapp.data.model.InventoryItem
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    val itemDao: ItemDao
) : Repository {

    private val items = mutableListOf<InventoryItem>()

    override suspend fun getAllItems() = items.toList()

    override suspend fun getItemsInAuditorium(auditorium: String) =
        items.filter { it.auditorium == auditorium }

    override suspend fun getItemById(id: String) = items.firstOrNull { it.id == id }

    override suspend fun getItemByBarcode(barcode: String) =
        items.firstOrNull { it.barcode == barcode }

    override suspend fun saveItem(item: InventoryItem) {
        items.find { it.id == item.id }?.let { items.remove(it) }
        items.add(item)
    }

    override suspend fun deleteItem(id: String) {
        getItemById(id)?.let { items.remove(it) }
    }
}
