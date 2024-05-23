package com.example.inventoryapp.data.api

interface Downloader {
    fun downloadFile(url: String): Long
}
