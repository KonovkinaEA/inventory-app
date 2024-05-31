package com.example.inventoryapp.data

import com.example.inventoryapp.data.api.AndroidDownloader
import com.example.inventoryapp.data.api.ApiService
import com.example.inventoryapp.data.db.DeleteDao
import com.example.inventoryapp.data.db.ItemDao
import com.example.inventoryapp.data.db.entities.DeleteIdEntity
import com.example.inventoryapp.data.model.InventoryItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class RepositoryTest {

    private val apiService = mockk<ApiService> {
        coEvery { updateItems(any()) } returns Response.success(apiItems)
        coEvery { updateItems(any(), any()) } returns Response.success(apiItems)
        coEvery { updateItem(any()) } returns Response.success(item)
        coEvery { addItem(any()) } returns Response.success(item)
        coEvery { getItem(any()) } returns Response.success(item)
        coEvery { deleteItem(any()) } returns Response.success(Unit)
        coEvery { generateReport(any()) } returns Response.success(Unit)
    }
    private val itemDao = mockk<ItemDao> {
        coEvery { findAllItems() } returns dbItems.map { it.toItemDbEntity() }
        coEvery { findItemsByLocation(any()) } returns dbItems.map { it.toItemDbEntity() }
        coEvery { replaceItems(any(), any()) } just runs
        coEvery { getItemById(any()) } returns item.toItemDbEntity()
        coEvery { updateItem(any()) } just runs
        coEvery { addItem(any()) } just runs
        coEvery { deleteItem(any()) } just runs
    }
    private val deleteDao = mockk<DeleteDao> {
        coEvery { getIds() } returns deleteIds
        coEvery { removeIds() } just runs
        coEvery { addId(any()) } just runs
    }
    private val downloader = mockk<AndroidDownloader> {
        every { downloadFile(any()) } returns 0L
    }

    private val repository: Repository = RepositoryImpl(apiService, itemDao, deleteDao, downloader)

    @Test
    fun `getItems returns dbData when api call fails`() = runTest {
        coEvery { apiService.updateItems(any()) } throws Exception()

        val result = repository.getItems()

        assertEquals(dbItems, result)
        coVerify(exactly = 1) { itemDao.findAllItems() }
        coVerify(exactly = 1) { deleteDao.getIds() }
        coVerify(exactly = 1) { apiService.updateItems(any()) }
    }

    @Test
    fun `getItems returns dbData when api response is not successful`() = runTest {
        coEvery { apiService.updateItems(any()) } returns
                Response.error(404, "".toResponseBody(null))

        val result = repository.getItems()

        assertEquals(dbItems, result)
        coVerify(exactly = 1) { itemDao.findAllItems() }
        coVerify(exactly = 1) { deleteDao.getIds() }
        coVerify(exactly = 1) { apiService.updateItems(any()) }
    }

    @Test
    fun `getItems returns updated list from API`() = runTest {
        val result = repository.getItems()

        assertEquals(apiItems, result)
        coVerify(exactly = 1) { itemDao.replaceItems(any(), isNull()) }
        coVerify(exactly = 1) { deleteDao.removeIds() }
    }

    @Test
    fun `getItems handles empty dbData and empty API response`() = runTest {
        coEvery { itemDao.findAllItems() } returns emptyList()
        coEvery { deleteDao.getIds() } returns emptyList()
        coEvery { apiService.updateItems(any()) } returns Response.success(emptyList())

        val result = repository.getItems()

        assertEquals(emptyList<InventoryItem>(), result)
        coVerify(exactly = 1) { itemDao.findAllItems() }
        coVerify(exactly = 1) { deleteDao.getIds() }
        coVerify(exactly = 1) { apiService.updateItems(any()) }
        coVerify(exactly = 1) { itemDao.replaceItems(emptyList(), isNull()) }
        coVerify(exactly = 1) { deleteDao.removeIds() }
    }

    @Test
    fun `getItemsByLocation returns dbData when api call fails`() = runTest {
        coEvery { apiService.updateItems(any(), any()) } throws Exception()

        val result = repository.getItemsByLocation(LOCATION)

        assertEquals(dbItems, result)
        coVerify(exactly = 1) { itemDao.findItemsByLocation(LOCATION) }
        coVerify(exactly = 1) { deleteDao.getIds() }
        coVerify(exactly = 1) { apiService.updateItems(any(), LOCATION) }
    }

    @Test
    fun `getItemsByLocation returns dbData when api response is not successful`() = runTest {
        coEvery { apiService.updateItems(any(), any()) } returns
                Response.error(404, "".toResponseBody(null))

        val result = repository.getItemsByLocation(LOCATION)

        assertEquals(dbItems, result)
        coVerify(exactly = 1) { itemDao.findItemsByLocation(LOCATION) }
        coVerify(exactly = 1) { deleteDao.getIds() }
        coVerify(exactly = 1) { apiService.updateItems(any(), LOCATION) }
    }

    @Test
    fun `getItemsByLocation returns updated list from API`() = runTest {
        val result = repository.getItemsByLocation(LOCATION)

        assertEquals(apiItems, result)
        coVerify(exactly = 1) { itemDao.replaceItems(any(), LOCATION) }
        coVerify(exactly = 1) { deleteDao.removeIds() }
    }

    @Test
    fun `getItemsByLocation handles empty dbData and empty API response`() = runTest {
        coEvery { itemDao.findItemsByLocation(LOCATION) } returns emptyList()
        coEvery { deleteDao.getIds() } returns emptyList()
        coEvery { apiService.updateItems(any(), any()) } returns Response.success(emptyList())

        val result = repository.getItemsByLocation(LOCATION)

        assertEquals(emptyList<InventoryItem>(), result)
        coVerify(exactly = 1) { itemDao.findItemsByLocation(LOCATION) }
        coVerify(exactly = 1) { deleteDao.getIds() }
        coVerify(exactly = 1) { apiService.updateItems(any(), LOCATION) }
        coVerify(exactly = 1) { itemDao.replaceItems(emptyList(), LOCATION) }
        coVerify(exactly = 1) { deleteDao.removeIds() }
    }

    @Test
    fun `saveItem updates existing item in db`() = runTest {
        repository.saveItem(item)

        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
        coVerify(exactly = 1) { apiService.updateItem(item) }
    }

    @Test
    fun `saveItem adds new item to db`() = runTest {
        coEvery { itemDao.getItemById(ID) } returns null
        coEvery { apiService.getItem(id = ID) } returns Response.success(null)

        repository.saveItem(item)

        coVerify(exactly = 1) { itemDao.addItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
        coVerify(exactly = 1) { apiService.addItem(item) }
    }

    @Test
    fun `saveItem updates item when API returns successful response`() = runTest {
        repository.saveItem(item)

        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
        coVerify(exactly = 1) { apiService.updateItem(item) }
    }

    @Test
    fun `saveItem adds item when API returns null body`() = runTest {
        coEvery { itemDao.getItemById(ID) } returns null
        coEvery { apiService.getItem(id = ID) } returns Response.success(null)

        repository.saveItem(item)

        coVerify(exactly = 1) { itemDao.addItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
        coVerify(exactly = 1) { apiService.addItem(item) }
    }

    @Test
    fun `saveItem handles API call exception`() = runTest {
        coEvery { apiService.getItem(id = ID) } throws Exception()

        repository.saveItem(item)

        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
        coVerify(exactly = 0) { apiService.updateItem(item) }
        coVerify(exactly = 0) { apiService.addItem(item) }
    }

    @Test
    fun `deleteItem deletes item from db and calls API`() = runTest {
        repository.deleteItem(ID)

        coVerify(exactly = 1) { itemDao.deleteItem(ID) }
        coVerify(exactly = 1) { deleteDao.addId(DeleteIdEntity(ID)) }
        coVerify(exactly = 1) { apiService.deleteItem(ID) }
    }

    @Test
    fun `deleteItem handles API call exception`() = runTest {
        coEvery { apiService.deleteItem(any()) } throws Exception()

        repository.deleteItem(ID)

        coVerify(exactly = 1) { itemDao.deleteItem(ID) }
        coVerify(exactly = 1) { deleteDao.addId(DeleteIdEntity(ID)) }
        coVerify(exactly = 1) { apiService.deleteItem(ID) }
    }

    @Test
    fun `deleteItem deletes item and adds to deleteDao even if API fails`() = runTest {
        coEvery { apiService.deleteItem(any()) } throws Exception()

        repository.deleteItem(ID)

        coVerify(exactly = 1) { itemDao.deleteItem(ID) }
        coVerify(exactly = 1) { deleteDao.addId(DeleteIdEntity(ID)) }
    }

    @Test
    fun `download all items data in excel file`() = runTest {
        repository.downloadItemsExcel("")
        coVerify(exactly = 1) { downloader.downloadFile(URL_DOWNLOAD) }
    }

    @Test
    fun `download items in location data in excel file`() = runTest {
        val location = "3-405"
        repository.downloadItemsExcel(location)

        coVerify(exactly = 1) { downloader.downloadFile("$URL_DOWNLOAD/$location") }
    }

    @Test
    fun `download report`() = runTest {
        repository.getReport(apiItems)

        coVerify(exactly = 1) { apiService.generateReport(apiItems) }
        coVerify(exactly = 1) { downloader.downloadFile(URL_REPORT) }
    }

    companion object {

        private const val ID = "id"
        private const val LOCATION = "location"
        private const val URL_DOWNLOAD = "http://192.168.1.139/api/v1/items/excel/download"
        private const val URL_REPORT = "http://192.168.1.139/api/v1/items/excel/report"

        private val deleteIds = listOf(DeleteIdEntity("id3"))
        private val dbItems = listOf(
            InventoryItem("id1", "code1", "name1", "location1"),
            InventoryItem("id2", "code2", "name2", "location2")
        )
        private val apiItems = listOf(
            InventoryItem("id3", "code3", "name3", "location3"),
            InventoryItem("id4", "code4", "name4", "location4")
        )
        private val item = InventoryItem("id", "code", "name", "location", "barcode")
    }
}
