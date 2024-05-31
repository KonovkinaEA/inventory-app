package com.example.inventoryapp.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "deleteId", indices = [Index("id")])
data class DeleteIdEntity(
    @PrimaryKey val id: String
)
