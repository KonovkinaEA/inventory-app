package com.example.inventoryapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventoryapp.data.db.AppDatabase
import com.example.inventoryapp.data.db.DeleteDao
import com.example.inventoryapp.data.db.ItemDao
import com.example.inventoryapp.data.db.entities.DeleteIdEntity
import com.example.inventoryapp.data.model.InventoryItem
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var itemDao: ItemDao
    private lateinit var deleteDao: DeleteDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        itemDao = db.getTodoItemDao()
        deleteDao = db.getDeleteDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndRetrieveItem() {
        val item = InventoryItem(name = "name").toItemDbEntity()
        itemDao.addItem(item)
        val retrievedItem = itemDao.getItemById(item.id)
        assertNotNull(retrievedItem)
        assertEquals(item.name, retrievedItem?.name)
    }

    @Test
    fun testInsertAndRetrieveDeleteId() {
        val deleteId = DeleteIdEntity(id = UUID.randomUUID().toString())
        deleteDao.addId(deleteId)
        val retrievedDeleteIds = deleteDao.getIds()
        assertNotNull(retrievedDeleteIds)
        assertEquals(deleteId.id, retrievedDeleteIds.first().id)
    }
}
