package com.example.inventoryapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.inventoryapp.data.db.entities.ItemDbEntity

@Dao
interface ItemDao {

    @Query(
        "SELECT item.id, name, barcode, inventory_num, code, auditorium, type_name FROM item\n" +
                "INNER JOIN item_types ON item.type_id = item_types.id"
    )
    fun getAllItems(): List<ItemInfoTuple>

    @Insert(entity = ItemDbEntity::class)
    fun addItem(item: ItemDbEntity)

    @Query("DELETE FROM item WHERE id = :id")
    fun deleteItem(id: String)

    @Update(entity = ItemDbEntity::class)
    fun updateItem(item: ItemDbEntity)
}
