package com.example.inventoryapp.data.api

import com.example.inventoryapp.data.model.InventoryItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("items")
    suspend fun getItems(): Response<List<InventoryItem>>
}
