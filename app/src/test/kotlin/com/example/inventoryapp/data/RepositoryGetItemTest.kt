package com.example.inventoryapp.data

import com.example.inventoryapp.data.api.ApiService
import com.example.inventoryapp.data.db.DeleteDao
import com.example.inventoryapp.data.db.ItemDao
import com.example.inventoryapp.data.model.InventoryItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryGetItemTest {

    private val apiService = mockk<ApiService>()
    private val itemDao = mockk<ItemDao>()
    private val deleteDao = mockk<DeleteDao>()

    private val repository: Repository = RepositoryImpl(apiService, itemDao, deleteDao)

    @Before
    fun setUp() {
        coEvery { apiService.getItem(id = any()) } returns Response.success(item)
        coEvery { apiService.getItem(code = any()) } returns Response.success(item)
        coEvery { apiService.getItem(inventoryNum = any()) } returns Response.success(item)
        coEvery { apiService.getItem(barcode = any()) } returns Response.success(item)
        coEvery { itemDao.getItemById(any()) } returns item.toItemDbEntity()
        coEvery { itemDao.getItemByCode(any()) } returns item.toItemDbEntity()
        coEvery { itemDao.getItemByInventoryNum(any()) } returns item.toItemDbEntity()
        coEvery { itemDao.getItemByBarcode(any()) } returns item.toItemDbEntity()
        coEvery { itemDao.updateItem(any()) } just runs
        coEvery { itemDao.addItem(any()) } just runs
    }

    @Test
    fun `getItemById returns dbData when api call fails`() = runTest {
        coEvery { apiService.getItem(id = ID) } throws Exception()

        val result = repository.getItemById(ID)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.getItemById(ID) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
    }

    @Test
    fun `getItemById returns dbData when api response is not successful`() = runTest {
        coEvery { apiService.getItem(id = ID) } returns
                Response.error(404, "".toResponseBody(null))

        val result = repository.getItemById(ID)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.getItemById(ID) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
    }

    @Test
    fun `getItemById returns updated item from API`() = runTest {
        val result = repository.getItemById(ID)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { itemDao.getItemById(ID) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
    }

    @Test
    fun `getItemById returns item from db if not present in API`() = runTest {
        coEvery { apiService.getItem(id = any()) } returns Response.success(null)

        val result = repository.getItemById(ID)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.getItemById(ID) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
    }

    @Test
    fun `getItemById adds item from API if not present in db`() = runTest {
        coEvery { itemDao.getItemById(any()) } returns null
        coEvery { itemDao.getItemByCode(any()) } returns null
        coEvery { itemDao.getItemByBarcode(any()) } returns null
        coEvery { itemDao.getItemByInventoryNum(any()) } returns null

        val result = repository.getItemById(ID)

        assertEquals(item, result)
        coVerify { itemDao.getItemById(ID) }
        coVerify(exactly = 1) { itemDao.addItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { apiService.getItem(id = ID) }
    }

    @Test
    fun `getItemByCode returns dbData when api call fails`() = runTest {
        coEvery { apiService.getItem(code = any()) } throws Exception()

        val result = repository.getItemByCode(CODE)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.getItemByCode(CODE) }
        coVerify(exactly = 1) { apiService.getItem(code = CODE) }
    }

    @Test
    fun `getItemByCode returns updated item from API`() = runTest {
        val result = repository.getItemByCode(CODE)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { itemDao.getItemByCode(CODE) }
        coVerify(exactly = 1) { apiService.getItem(code = CODE) }
    }

    @Test
    fun `getItemByCode update item in db by finding it by other parameter`() = runTest {
        coEvery { itemDao.getItemByCode(any()) } returns null

        val result = repository.getItemByCode(CODE)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { itemDao.getItemByCode(CODE) }
        coVerify(exactly = 1) { apiService.getItem(code = CODE) }
    }

    @Test
    fun `getItemByInventoryNum returns dbData when api call fails`() = runTest {
        coEvery { apiService.getItem(inventoryNum = any()) } throws Exception()

        val result = repository.getItemByInventoryNum(INVENTORY_NUM)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.getItemByInventoryNum(INVENTORY_NUM) }
        coVerify(exactly = 1) { apiService.getItem(inventoryNum = INVENTORY_NUM) }
    }

    @Test
    fun `getItemByInventoryNum returns updated item from API`() = runTest {
        val result = repository.getItemByInventoryNum(INVENTORY_NUM)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { itemDao.getItemByInventoryNum(INVENTORY_NUM) }
        coVerify(exactly = 1) { apiService.getItem(inventoryNum = INVENTORY_NUM) }
    }

    @Test
    fun `getItemByBarcode returns dbData when api call fails`() = runTest {
        coEvery { apiService.getItem(barcode = any()) } throws Exception()

        val result = repository.getItemByBarcode(BARCODE)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.getItemByBarcode(BARCODE) }
        coVerify(exactly = 1) { apiService.getItem(barcode = BARCODE) }
    }

    @Test
    fun `getItemByBarcode returns updated item from API`() = runTest {
        val result = repository.getItemByBarcode(BARCODE)

        assertEquals(item, result)
        coVerify(exactly = 1) { itemDao.updateItem(item.toItemDbEntity()) }
        coVerify(exactly = 1) { itemDao.getItemByBarcode(BARCODE) }
        coVerify(exactly = 1) { apiService.getItem(barcode = BARCODE) }
    }

    companion object {

        private const val ID = "id"
        private const val CODE = "code"
        private const val INVENTORY_NUM = "inventoryNum"
        private const val BARCODE = "barcode"

        private val item = InventoryItem("id", "code", "name", "location", "barcode")
    }
}
