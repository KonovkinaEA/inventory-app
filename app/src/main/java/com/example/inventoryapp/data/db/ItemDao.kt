package com.example.inventoryapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.inventoryapp.data.db.entities.ItemDbEntity

@Dao
interface ItemDao {

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n"
    )
    fun findAllItems(): List<ItemDbEntity>

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n" +
                "WHERE item.name = :name"
    )
    fun findItemsByName(name: String): List<ItemDbEntity>

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n" +
                "WHERE item.location = :location"
    )
    fun findItemsByLocation(location: String): List<ItemDbEntity>

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n" +
                "WHERE item.last_updated_by = :lastUpdatedBy"
    )
    fun findItemsByLastUpdatedBy(lastUpdatedBy: String): List<ItemDbEntity>

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n" +
                "WHERE item.id = :id"
    )
    fun getItemById(id: String): ItemDbEntity?

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n" +
                "WHERE item.code = :code"
    )
    fun getItemByCode(code: String): ItemDbEntity?

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n" +
                "WHERE item.inventory_num = :inventoryNum"
    )
    fun getItemByInventoryNum(inventoryNum: String): ItemDbEntity?

    @Query(
        "SELECT item.id, name, code, inventory_num, barcode, manufacture_date, factory_num," +
                "building, location, count, changed_at, last_updated_by, revision FROM item\n" +
                "WHERE item.barcode = :barcode"
    )
    fun getItemByBarcode(barcode: String): ItemDbEntity?

    @Insert(entity = ItemDbEntity::class)
    fun addItem(item: ItemDbEntity)

    @Query("DELETE FROM item WHERE id = :id")
    fun deleteItem(id: String)

    @Update(entity = ItemDbEntity::class)
    fun updateItem(item: ItemDbEntity)
}
