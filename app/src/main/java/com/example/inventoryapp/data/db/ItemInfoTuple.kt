package com.example.inventoryapp.data.db

import androidx.room.ColumnInfo
import com.example.inventoryapp.data.model.InventoryItem

data class ItemInfoTuple(
    val id: String,
    val name: String,
    val barcode: String?,
    @ColumnInfo(name = "inventory_num") val inventoryNum: String?,
    val code: String?,
    val auditorium: String?,
    @ColumnInfo(name = "type_name") val type: String?
) {

    fun toInventoryItem() = InventoryItem(
        id = id,
        barcode = barcode ?: "",
        code = code ?: "",
        number = inventoryNum ?: "",
        auditorium = auditorium ?: ""
    )
}
