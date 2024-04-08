package com.example.inventoryapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item",
    indices = [Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = ItemTypesDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["type_id"]
        )
    ]
)
data class ItemDbEntity(
    @PrimaryKey val id: String,
    val name: String,
    val barcode: String?,
    @ColumnInfo(name = "inventory_num") val inventoryNum: String?,
    val code: String?,
    val auditorium: String?,
    @ColumnInfo(name = "type_id") val typeId: Int?
)
