package com.example.inventoryapp.data

import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.common.Barcode.CalendarEvent
import com.google.mlkit.vision.barcode.common.Barcode.ContactInfo
import com.google.mlkit.vision.barcode.common.Barcode.DriverLicense
import com.google.mlkit.vision.barcode.common.Barcode.Email
import com.google.mlkit.vision.barcode.common.Barcode.GeoPoint
import com.google.mlkit.vision.barcode.common.Barcode.Phone
import com.google.mlkit.vision.barcode.common.Barcode.Sms
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_CALENDAR_EVENT
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_CONTACT_INFO
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_DRIVER_LICENSE
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_EMAIL
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_GEO
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_ISBN
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_PHONE
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_PRODUCT
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_SMS
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_TEXT
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_UNKNOWN
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_URL
import com.google.mlkit.vision.barcode.common.Barcode.TYPE_WIFI
import com.google.mlkit.vision.barcode.common.Barcode.WiFi
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@ExperimentalCoroutinesApi
@RunWith(Parameterized::class)
class ScannerManagerTest(
    private val barcode: Barcode,
    private val expectedResult: String
) {

    private val ioDispatcher = StandardTestDispatcher()
    private val scanner: GmsBarcodeScanner = mockk()

    private lateinit var scannerManager: ScannerManager

    @Before
    fun setUp() {
        Dispatchers.setMain(ioDispatcher)
        scannerManager = ScannerManagerImpl(ioDispatcher, scanner)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful scan`() = runTest {
        val mockTask = mockk<Task<Barcode>>()
        every { mockTask.addOnSuccessListener(any()) } answers {
            firstArg<OnSuccessListener<Barcode>>().onSuccess(barcode)
            mockTask
        }
        every { mockTask.addOnFailureListener(any()) } returns mockTask
        every { scanner.startScan() } returns mockTask

        val result = scannerManager.scanningData.first()
        assert(result == expectedResult)
    }

    companion object {

        private const val URL = "url"
        private const val DISPLAY_VALUE = "displayValue"
        private const val RAW_VALUE = "rawValue"

        private val email = mockk<Email>()
        private val contactInfo = mockk<ContactInfo>()
        private val phone = mockk<Phone>()
        private val calendarEvent = mockk<CalendarEvent>()
        private val geoPoint = mockk<GeoPoint>()
        private val driverLicense = mockk<DriverLicense>()
        private val sms = mockk<Sms>()
        private val wifi = mockk<WiFi>()

        @JvmStatic
        @Parameterized.Parameters
        fun params() = listOf(
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_URL
                every { url?.url } returns URL
            }, URL),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_PRODUCT
                every { displayValue } returns DISPLAY_VALUE
            }, DISPLAY_VALUE),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_EMAIL
                every { email } returns this@Companion.email
            }, "$email"),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_CONTACT_INFO
                every { contactInfo } returns this@Companion.contactInfo
            }, "$contactInfo"),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_PHONE
                every { phone } returns this@Companion.phone
            }, "$phone"),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_CALENDAR_EVENT
                every { calendarEvent } returns this@Companion.calendarEvent
            }, "$calendarEvent"),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_GEO
                every { geoPoint } returns this@Companion.geoPoint
            }, "$geoPoint"),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_ISBN
                every { displayValue } returns DISPLAY_VALUE
            }, DISPLAY_VALUE),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_DRIVER_LICENSE
                every { driverLicense } returns this@Companion.driverLicense
            }, "$driverLicense"),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_SMS
                every { sms } returns this@Companion.sms
            }, "$sms"),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_TEXT
                every { rawValue } returns RAW_VALUE
            }, RAW_VALUE),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_UNKNOWN
                every { rawValue } returns RAW_VALUE
            }, RAW_VALUE),
            arrayOf(mockk<Barcode> {
                every { valueType } returns TYPE_WIFI
                every { wifi } returns this@Companion.wifi
            }, "Couldn't determine")
        )
    }
}
