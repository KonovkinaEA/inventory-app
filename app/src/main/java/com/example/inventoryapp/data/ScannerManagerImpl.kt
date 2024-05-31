package com.example.inventoryapp.data

import com.example.inventoryapp.di.IoDispatcher
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScannerManagerImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val scanner: GmsBarcodeScanner
) : ScannerManager {

    override val scanningData: Flow<String?>
        get() = callbackFlow {
            scanner.startScan()
                .addOnSuccessListener {
                    launch(ioDispatcher) { send(getDetails(it)) }
                }.addOnFailureListener {
                    launch(ioDispatcher) { it.printStackTrace() }
                }
            awaitClose { }
        }

    private fun getDetails(barcode: Barcode): String {
        return when (barcode.valueType) {
            Barcode.TYPE_URL -> "${barcode.url?.url}"
            Barcode.TYPE_PRODUCT -> "${barcode.displayValue}"
            Barcode.TYPE_EMAIL -> "${barcode.email}"
            Barcode.TYPE_CONTACT_INFO -> "${barcode.contactInfo}"
            Barcode.TYPE_PHONE -> "${barcode.phone}"
            Barcode.TYPE_CALENDAR_EVENT -> "${barcode.calendarEvent}"
            Barcode.TYPE_GEO -> "${barcode.geoPoint}"
            Barcode.TYPE_ISBN -> "${barcode.displayValue}"
            Barcode.TYPE_DRIVER_LICENSE -> "${barcode.driverLicense}"
            Barcode.TYPE_SMS -> "${barcode.sms}"
            Barcode.TYPE_TEXT -> "${barcode.rawValue}"
            Barcode.TYPE_UNKNOWN -> "${barcode.rawValue}"
            else -> "Couldn't determine"
        }
    }
}
