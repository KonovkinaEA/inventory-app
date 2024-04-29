package com.example.inventoryapp.data

import com.example.inventoryapp.data.db.ItemDao
import com.example.inventoryapp.data.model.InventoryItem
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val itemDao: ItemDao
) : Repository {

    private var cachedItems: MutableList<InventoryItem> = mutableListOf()

    override suspend fun getAllItems(): List<InventoryItem> {
        val list = itemDao.findAllItems().map { it.toInventoryItem() }
        cachedItems = list.toMutableList()
        return list
    }

    override suspend fun getItemsByLocation(location: String): List<InventoryItem> {
        val list = itemDao.findItemsByLocation(location).map { it.toInventoryItem() }
        cachedItems = list.toMutableList()
        return list
    }

    override suspend fun getItemById(id: String) =
        cachedItems.firstOrNull { it.id == id }
            ?: itemDao.getItemById(id)?.toInventoryItem()
                ?.also { if (!cachedItems.contains(it)) cachedItems.add(it) }

    override suspend fun getItemByCode(code: String) =
        cachedItems.firstOrNull { it.code == code }
            ?: itemDao.getItemByCode(code)?.toInventoryItem()
                ?.also { if (!cachedItems.contains(it)) cachedItems.add(it) }

    override suspend fun getItemByInventoryNum(inventoryNum: String) =
        cachedItems.firstOrNull { it.inventoryNum == inventoryNum }
            ?: itemDao.getItemByInventoryNum(inventoryNum)?.toInventoryItem()
                ?.also { if (!cachedItems.contains(it)) cachedItems.add(it) }

    override suspend fun getItemByBarcode(barcode: String) =
        cachedItems.firstOrNull { it.barcode == barcode }
            ?: itemDao.getItemByBarcode(barcode)?.toInventoryItem()
                ?.also { if (!cachedItems.contains(it)) cachedItems.add(it) }

    override suspend fun saveItem(item: InventoryItem) {
        cachedItems = cachedItems.map { if (it.id == item.id) item else it }.toMutableList()

        val dbItem = item.toItemDbEntity()
        if (itemDao.getItemById(item.id) != null) {
            itemDao.updateItem(dbItem)
        } else {
            itemDao.addItem(dbItem)
        }
    }

    override suspend fun deleteItem(id: String) {
        cachedItems.removeAll { it.id == id }
        itemDao.deleteItem(id)
    }
}
