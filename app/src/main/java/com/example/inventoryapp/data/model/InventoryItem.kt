package com.example.inventoryapp.data.model

import com.example.inventoryapp.data.db.entities.ItemDbEntity
import java.util.UUID

data class InventoryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val barcode: String = "",
    val code: String = "",
    val number: String = "",
    val auditorium: String = "",
    val type: ItemType = ItemType.DEFAULT
) {

    fun toItemDbEntity() = ItemDbEntity(
        id = id,
        name = name,
        barcode = barcode,
        inventoryNum = number,
        code = code,
        auditorium = auditorium,
        typeId = type.id
    )
}
