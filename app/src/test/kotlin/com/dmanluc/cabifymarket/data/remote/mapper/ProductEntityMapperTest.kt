package com.dmanluc.cabifymarket.data.remote.mapper

import android.content.res.AssetManager
import com.dmanluc.cabifymarket.domain.entity.BulkDiscountRule
import com.dmanluc.cabifymarket.domain.entity.FreePerQuantityDiscountRule
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.utils.MockDataProvider
import com.dmanluc.cabifymarket.utils.MockDataProvider.readJsonAsString
import com.dmanluc.cabifymarket.utils.readJsonAssetFileName
import com.google.gson.GsonBuilder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verifyOrder
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * @author Daniel Manrique Lucas <dmanluc91></dmanluc91>@gmail.com>
 * @version 1
 * @since 2019-09-10.
 */
class ProductEntityMapperTest {

    private val gson = GsonBuilder().create()
    private val assetManager = mockk<AssetManager>()

    private lateinit var mapper: ProductEntityMapper

    @Before
    fun setUp() {
        mockkStatic("com.dmanluc.cabifymarket.utils.ExtensionsKt")
        every { any<AssetManager>().readJsonAssetFileName(any()) } returns readJsonAsString("productDiscountRules.json")

        mapper = ProductEntityMapper(gson, assetManager)
    }

    @Test
    fun `map market api response to domain product list`() {
        val mockMarketApiResponse = MockDataProvider.createMockMarketProductsApiResponse()

        val result = mapper.mapFrom(mockMarketApiResponse)

        verifyOrder {
            assetManager.readJsonAssetFileName(any())
        }

        Assert.assertEquals(result.size, mockMarketApiResponse.products?.size)

        with(result[0]) {
            Assert.assertThat(this.type, `is`(Product.Type.MUG))
            Assert.assertThat(this.discountRule, nullValue())
        }

        with(result[1]) {
            Assert.assertThat(this.type, `is`(Product.Type.TSHIRT))
            Assert.assertThat(this.discountRule, instanceOf(BulkDiscountRule::class.java))
        }

        with(result[2]) {
            Assert.assertThat(this.type, `is`(Product.Type.VOUCHER))
            Assert.assertThat(
                this.discountRule,
                instanceOf(FreePerQuantityDiscountRule::class.java)
            )
        }
    }

}