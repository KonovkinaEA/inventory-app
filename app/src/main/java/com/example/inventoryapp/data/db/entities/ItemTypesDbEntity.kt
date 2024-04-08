package com.example.inventoryapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_types")
data class ItemTypesDbEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "type_name") val typeName: String
)
