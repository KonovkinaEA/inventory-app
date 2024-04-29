package com.example.inventoryapp.data.model

import com.example.inventoryapp.data.db.entities.ItemDbEntity
import java.util.Date
import java.util.UUID

data class InventoryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val code: String = "",
    val inventoryNum: String = "",
    val barcode: String = "",
    val manufactureDate: Long = 0,
    val factoryNum: String = "",
    val building: String = "",
    val location: String = "",
    val count: Int = 0,
    val changedAt: Long = Date().time,
    val lastUpdatedBy: String = "",
    val revision: Long = 0
) {

    fun toItemDbEntity() = ItemDbEntity(
        id = id,
        name = name,
        code = code,
        inventoryNum = inventoryNum,
        barcode = barcode,
        manufactureDate = manufactureDate,
        factoryNum = factoryNum,
        building = building,
        location = location,
        count = count,
        changedAt = changedAt,
        lastUpdatedBy = lastUpdatedBy,
        revision = revision
    )
}
