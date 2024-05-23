package com.example.inventoryapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.inventoryapp.data.model.InventoryItem

@Entity(tableName = "item", indices = [Index("id")])
data class ItemDbEntity(
    @PrimaryKey val id: String,
    val name: String,
    val code: String,
    @ColumnInfo(name = "inventory_num") val inventoryNum: String,
    val barcode: String,
    @ColumnInfo(name = "manufacture_date") val manufactureDate: Long?,
    @ColumnInfo(name = "factory_num") val factoryNum: String,
    val building: String,
    val location: String,
    @ColumnInfo(name = "is_correctly_placed") val isCorrectlyPlaced: Boolean,
    val count: Int?,
    @ColumnInfo(name = "changed_at") val changedAt: Long,
    @ColumnInfo(name = "last_updated_by") val lastUpdatedBy: String,
    val revision: Long
) {

    fun toInventoryItem() = InventoryItem(
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
        lastUpdatedBy = lastUpdatedBy,
        revision = revision
    )
}
