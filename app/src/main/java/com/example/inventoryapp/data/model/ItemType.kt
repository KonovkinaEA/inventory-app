package com.example.inventoryapp.data.model

enum class ItemType(val dbValue: String, val uiValue: String) {
    SCREEN("screen", "Монитор"),
    SYSTEM_UNIT("system unit", "Системный блок"),
    FURNITURE("furniture", "Мебель"),
    OTHER("other", "Разное"),
    DEFAULT("default", "-")
}
