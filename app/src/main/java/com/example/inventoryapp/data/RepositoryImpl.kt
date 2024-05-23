package com.example.inventoryapp.data

import com.example.inventoryapp.data.api.AndroidDownloader
import com.example.inventoryapp.data.api.ApiService
import com.example.inventoryapp.data.api.model.UpdatedData
import com.example.inventoryapp.data.db.DeleteDao
import com.example.inventoryapp.data.db.ItemDao
import com.example.inventoryapp.data.db.entities.DeleteIdEntity
import com.example.inventoryapp.data.model.InventoryItem
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val itemDao: ItemDao,
    private val deleteDao: DeleteDao,
    private val downloader: AndroidDownloader
) : Repository {

    override suspend fun getItems(): List<InventoryItem> {
        val dbData = itemDao.findAllItems().map { it.toInventoryItem() }
        return try {
            val response =
                apiService.updateItems(UpdatedData(dbData, deleteDao.getIds().map { it.id }))
            updateList(response, dbData)
        } catch (_: Exception) {
            dbData
        }
    }

    override suspend fun getItemsByLocation(location: String): List<InventoryItem> {
        val dbData = itemDao.findItemsByLocation(location).map { it.toInventoryItem() }
        return try {
            val response = apiService.updateItems(
                UpdatedData(dbData, deleteDao.getIds().map { it.id }),
                location
            )
            updateList(response, dbData, location)
        } catch (_: Exception) {
            dbData
        }
    }

    override suspend fun getItemById(id: String): InventoryItem? {
        val dbData = itemDao.getItemById(id)?.toInventoryItem()
        return try {
            updateItem(apiService.getItem(id = id), dbData)
        } catch (_: Exception) {
            dbData
        }
    }

    override suspend fun getItemByCode(code: String): InventoryItem? {
        val dbData = itemDao.getItemByCode(code)?.toInventoryItem()
        return try {
            updateItem(apiService.getItem(code = code), dbData)
        } catch (_: Exception) {
            dbData
        }
    }

    override suspend fun getItemByInventoryNum(inventoryNum: String): InventoryItem? {
        val dbData = itemDao.getItemByInventoryNum(inventoryNum)?.toInventoryItem()
        return try {
            updateItem(apiService.getItem(inventoryNum = inventoryNum), dbData)
        } catch (_: Exception) {
            dbData
        }
    }

    override suspend fun getItemByBarcode(barcode: String): InventoryItem? {
        val dbData = itemDao.getItemByBarcode(barcode)?.toInventoryItem()
        return try {
            updateItem(apiService.getItem(barcode = barcode), dbData)
        } catch (_: Exception) {
            dbData
        }
    }

    override suspend fun saveItem(item: InventoryItem) {
        val dbItem = item.toItemDbEntity()
        if (itemDao.getItemById(item.id) != null) {
            itemDao.updateItem(dbItem)
        } else {
            itemDao.addItem(dbItem)
        }

        try {
            val response = apiService.getItem(id = item.id)
            if (response.isSuccessful) {
                val serverData = response.body()
                if (serverData != null) {
                    apiService.updateItem(item)
                } else {
                    apiService.addItem(item)
                }
            }
        } catch (_: Exception) {
        }
    }

    override suspend fun deleteItem(id: String) {
        itemDao.deleteItem(id)
        deleteDao.addId(DeleteIdEntity(id))
        try {
            apiService.deleteItem(id)
        } catch (_: Exception) {
        }
    }

    override suspend fun downloadItemsExcel(location: String?) {
        if (location != null) {
            downloader.downloadFile("http://192.168.1.139/api/v1/items/excel/download/${location}")
        } else {
            downloader.downloadFile("http://192.168.1.139/api/v1/items/excel/download")
        }
    }

    private fun updateList(
        response: Response<List<InventoryItem>>,
        dbData: List<InventoryItem>,
        location: String? = null
    ) = if (response.isSuccessful) {
        val serverData = response.body() as List<InventoryItem>
        itemDao.replaceItems(serverData.map { it.toItemDbEntity() }, location)
        deleteDao.removeIds()
        serverData
    } else {
        dbData
    }

    private fun updateItem(response: Response<InventoryItem>, dbData: InventoryItem?) =
        if (response.isSuccessful) {
            val serverData = response.body()
            if (serverData != null) {
                if (dbData != null) {
                    itemDao.updateItem(serverData.toItemDbEntity())
                } else {
                    if (tryGetItemByAllFields(serverData) != null) {
                        itemDao.updateItem(serverData.toItemDbEntity())
                    } else {
                        itemDao.addItem(serverData.toItemDbEntity())
                    }
                }
                serverData
            } else {
                dbData
            }
        } else {
            dbData
        }

    private fun tryGetItemByAllFields(item: InventoryItem) =
        itemDao.getItemById(item.id)?.toInventoryItem() ?: itemDao.getItemByCode(item.code)
            ?.toInventoryItem() ?: itemDao.getItemByBarcode(item.barcode)?.toInventoryItem()
        ?: itemDao.getItemByInventoryNum(item.inventoryNum)?.toInventoryItem()
}
