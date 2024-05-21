package com.example.inventoryapp.data.api

import com.example.inventoryapp.data.api.model.UpdatedData
import com.example.inventoryapp.data.model.InventoryItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @PATCH("items")
    suspend fun updateItems(
        @Body list: UpdatedData,
        @Query("location") location: String? = null
    ): Response<List<InventoryItem>>

    @GET("items/item")
    suspend fun getItem(
        @Query("id") id: String? = null,
        @Query("code") code: String? = null,
        @Query("inventoryNum") inventoryNum: String? = null,
        @Query("barcode") barcode: String? = null
    ): Response<InventoryItem>

    @POST("items/item")
    suspend fun addItem(@Body item: InventoryItem): Response<InventoryItem>

    @PUT("items/item")
    suspend fun updateItem(@Body item: InventoryItem): Response<InventoryItem>

    @DELETE("items/item")
    suspend fun deleteItem(@Query("id") id: String): Response<Unit>
}
