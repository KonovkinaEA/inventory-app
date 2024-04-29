package com.example.inventoryapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inventoryapp.data.db.entities.ItemDbEntity

@Database(
    version = 3,
    entities = [ItemDbEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoItemDao(): ItemDao

    companion object {

        private var instance: AppDatabase? = null
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            Room.databaseBuilder(context, AppDatabase::class.java, "database.db")
                .createFromAsset("inventory_db.db")
                .build()
        }
    }
}
