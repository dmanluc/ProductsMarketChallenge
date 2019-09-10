package com.dmanluc.cabifymarket.data.remote.api

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import retrofit2.HttpException
import java.net.HttpURLConnection

/**
 * @author Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version 1
 * @since 2019-08-31.
 */
class MarketApiTest : BaseApiTest() {

    @Test
    fun `get products from market`() {
        mockHttpResponse(mockServer, "marketProducts.json", HttpURLConnection.HTTP_OK)
        runBlocking {
            val productsApiResponse = apiService.getProductsAsync().await()
            with(productsApiResponse) {
                Assert.assertEquals(true, products != null)
                Assert.assertEquals(3, products?.size)
                Assert.assertEquals("VOUCHER", products?.first()?.id)
                Assert.assertEquals("Cabify Voucher", products?.first()?.name)
                Assert.assertEquals(5.0, products?.first()?.price)
            }
        }
    }

    @Test(expected = HttpException::class)
    fun `get products from market with http exception`() {
        mockHttpResponse(mockServer, "marketProducts.json", HttpURLConnection.HTTP_FORBIDDEN)
        runBlocking {
            apiService.getProductsAsync().await()
        }
    }

}