package com.example.inventoryapp.data

import kotlinx.coroutines.flow.Flow

interface Repository {

    val scanningData: Flow<String?>
}
