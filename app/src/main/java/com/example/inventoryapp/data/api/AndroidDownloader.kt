package com.example.inventoryapp.data.api

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

class AndroidDownloader(context: Context) {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/vnd.ms-excel")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Inventory.xls")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Inventory.xls")

        return downloadManager.enqueue(request)
    }
}
