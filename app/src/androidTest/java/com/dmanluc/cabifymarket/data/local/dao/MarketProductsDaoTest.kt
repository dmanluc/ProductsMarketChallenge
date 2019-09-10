package com.dmanluc.cabifymarket.data.local.dao

import com.dmanluc.cabifymarket.utils.MockDataProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-09-10.
 */
class MarketProductsDaoTest : BaseDaoTest() {

    private val mockProductEntities = MockDataProvider.createMockProductEntities()

    override fun setUp() {
        super.setUp()
        fillDatabase()
    }

    @Test
    fun getMarketProducts_shouldReturnThem() = runBlocking {
        val productEntities = database.marketProductsDao().getMarketProducts()
        Assert.assertEquals(3, productEntities.size)
        Assert.assertEquals(mockProductEntities.first(), productEntities.first())
    }

    @Test
    fun saveMarketProducts_shouldUpdateDataInDatabase() = runBlocking {
        val newMockProductEntities =
            mockProductEntities.map { it.copy(name = it.name.toUpperCase()) }

        database.marketProductsDao().save(newMockProductEntities)
        val newProductEntities = database.marketProductsDao().getMarketProducts()

        Assert.assertNotEquals(mockProductEntities, newProductEntities)
    }

    private fun fillDatabase() {
        runBlocking {
            database.marketProductsDao().save(mockProductEntities)
        }
    }

}