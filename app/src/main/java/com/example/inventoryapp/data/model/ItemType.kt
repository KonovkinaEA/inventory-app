package com.example.inventoryapp.data.model

enum class ItemType(val id: Int, val dbValue: String, val uiValue: String) {
    SCREEN(0, "screen", "Монитор"),
    SYSTEM_UNIT(1, "system unit", "Системный блок"),
    FURNITURE(2, "furniture", "Мебель"),
    OTHER(3, "other", "Разное"),
    DEFAULT(4, "default", "-");

    companion object {

        fun fromDbValue(value: String?): ItemType {
            return value?.let { db ->
                entries.find { it.dbValue == db } ?: DEFAULT
            } ?: DEFAULT
        }
    }
}
