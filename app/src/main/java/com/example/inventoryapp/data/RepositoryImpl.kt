package com.example.inventoryapp.data

import com.example.inventoryapp.data.model.InventoryItem
import javax.inject.Inject

class RepositoryImpl @Inject constructor() : Repository {

    private val items = mutableListOf<InventoryItem>()

    override fun getAllItems() = items.toList()

    override fun getItemsInAuditorium(auditorium: String) =
        items.filter { it.auditorium == auditorium }

    override fun getItemById(id: String) = items.firstOrNull { it.id == id }

    override fun getItemByBarcode(barcode: String) = items.firstOrNull { it.barcode == barcode }

    override fun saveItem(item: InventoryItem) {
        items.find { it.id == item.id }?.let { items.remove(it) }
        items.add(item)
    }

    override fun deleteItem(id: String) {
        getItemById(id)?.let { items.remove(it) }
    }
}
