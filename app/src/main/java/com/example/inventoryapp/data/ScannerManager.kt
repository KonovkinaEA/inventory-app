package com.example.inventoryapp.data

import kotlinx.coroutines.flow.Flow

interface ScannerManager {

    val scanningData: Flow<String?>
}
