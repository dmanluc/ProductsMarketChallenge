package com.dmanluc.cabifymarket.data.remote.utils

import com.dmanluc.cabifymarket.data.remote.model.MarketApiResponse
import com.dmanluc.cabifymarket.domain.entity.BulkDiscountRule
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.FreePerQuantityDiscountRule
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import java.io.File

object MockDataProvider {

    fun createMockProductList(): List<Product> {
        return listOf(
            Product(
                type = Product.Type.MUG,
                name = "name1",
                price = CurrencyAmount(4.0),
                imageUrl = "https://cdn.shopify.com/s/files/1/0312/6537/products/27514-Black-White-1_aef806de-0299-4603-9305-bcc83155db8f_1024x1024.jpg?v=1495633232",
                discountRule = null
            ), Product(
                type = Product.Type.TSHIRT,
                name = "name2",
                price = CurrencyAmount(6.0),
                imageUrl = "https://www.goalinn.com/f/13608/136088796/adidas-real-madrid-away-16-17.jpg",
                discountRule = BulkDiscountRule("TSHIRT", "Buy 3 or more at 19€ per unit", 3, 19.0)
            ), Product(
                type = Product.Type.VOUCHER,
                name = "name3",
                price = CurrencyAmount(8.0),
                imageUrl = "https://i.rafitamolin.com/18cfea7.png",
                discountRule = FreePerQuantityDiscountRule("VOUCHER", "Special Offer 2x1", 2, 1)
            )
        )
    }

    fun createMockProductsCart(): ProductsCart {
        return ProductsCart(
            linkedMapOf(
                Product(
                    type = Product.Type.MUG,
                    name = "name1",
                    price = CurrencyAmount(4.0),
                    imageUrl = "https://cdn.shopify.com/s/files/1/0312/6537/products/27514-Black-White-1_aef806de-0299-4603-9305-bcc83155db8f_1024x1024.jpg?v=1495633232",
                    discountRule = null
                ) to 1,
                Product(
                    type = Product.Type.TSHIRT,
                    name = "name2",
                    price = CurrencyAmount(6.0),
                    imageUrl = "https://www.goalinn.com/f/13608/136088796/adidas-real-madrid-away-16-17.jpg",
                    discountRule = BulkDiscountRule(
                        "TSHIRT",
                        "Buy 3 or more at 19€ per unit",
                        3,
                        19.0
                    )
                ) to 2,
                Product(
                    type = Product.Type.VOUCHER,
                    name = "name3",
                    price = CurrencyAmount(8.0),
                    imageUrl = "https://i.rafitamolin.com/18cfea7.png",
                    discountRule = FreePerQuantityDiscountRule("VOUCHER", "Special Offer 2x1", 2, 1)
                ) to 3
            )
        )
    }

    fun createMockMarketProductsApiResponse(): MarketApiResponse {
        return MarketApiResponse(
            listOf(
                MarketApiResponse.ProductApiResponse(
                    id = "MUG", name = "name1", price = 4.0
                ), MarketApiResponse.ProductApiResponse(
                    id = "TSHIRT", name = "name2", price = 6.0
                ), MarketApiResponse.ProductApiResponse(
                    id = "VOUCHER", name = "name3", price = 8.0
                )
            )
        )
    }

    fun readJsonAsString(path: String): String {
        val uri = this.javaClass.classLoader.getResource(path)
        val file = File(uri.path)
        return String(file.readBytes())
    }

}