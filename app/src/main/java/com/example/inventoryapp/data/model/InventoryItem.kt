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
    val manufactureDate: Long? = null,
    val factoryNum: String = "",
    val building: String = "",
    val location: String = "",
    val isCorrectlyPlaced: Boolean = true,
    val count: Int? = null,
    val changedAt: Long = Date().time,
    val revision: Long = -1
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
        isCorrectlyPlaced = isCorrectlyPlaced,
        count = count,
        changedAt = changedAt,
        revision = revision
    )
}
