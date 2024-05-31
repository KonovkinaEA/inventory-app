package com.example.inventoryapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.inventoryapp.data.db.entities.DeleteIdEntity

@Dao
interface DeleteDao {

    @Query("SELECT deleteId.id FROM deleteId")
    fun getIds(): List<DeleteIdEntity>

    @Insert(entity = DeleteIdEntity::class)
    fun addId(id: DeleteIdEntity)

    @Query("DELETE FROM deleteId")
    fun removeIds()
}
