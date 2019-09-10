package com.dmanluc.cabifymarket.data.local.dao

import com.dmanluc.cabifymarket.data.local.model.ShoppingCartEntity
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.utils.MockDataProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-09-10.
 */
class ShoppingCartDaoTest : BaseDaoTest() {

    private val mockProductCartEntity = MockDataProvider.createMockProductsCartEntity()

    override fun setUp() {
        super.setUp()
        fillDatabase()
    }

    @Test
    fun getProductsCart_shouldReturnIt() = runBlocking {
        val productCartEntity = database.shoppingCartDao().getShoppingCart()
        Assert.assertEquals(3, productCartEntity.cart.size)
        Assert.assertEquals(mockProductCartEntity.cart, productCartEntity.cart)
    }

    @Test
    fun saveMarketProducts_shouldUpdateDataInDatabase() = runBlocking {
        val newMockProductCartEntity = ShoppingCartEntity(mockProductCartEntity.cart.mapValues { Random.nextInt() } as LinkedHashMap<Product, Int>)

        database.shoppingCartDao().saveShoppingCart(newMockProductCartEntity)
        val newProductCartEntity = database.shoppingCartDao().getShoppingCart()

        Assert.assertNotEquals(mockProductCartEntity, newProductCartEntity)
    }

    private fun fillDatabase() {
        runBlocking {
            database.shoppingCartDao().saveShoppingCart(mockProductCartEntity)
        }
    }

}