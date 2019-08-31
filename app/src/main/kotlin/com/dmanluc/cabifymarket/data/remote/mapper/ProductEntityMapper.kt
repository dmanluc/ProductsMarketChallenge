package com.dmanluc.cabifymarket.data.remote.mapper

import android.content.res.AssetManager
import com.dmanluc.cabifymarket.data.remote.model.DiscountRuleResponse
import com.dmanluc.cabifymarket.data.remote.model.MarketApiResponse
import com.dmanluc.cabifymarket.domain.entity.BulkDiscountRule
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.FreePerQuantityDiscountRule
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.utils.EntityMapper
import com.dmanluc.cabifymarket.utils.readJsonAssetFileName
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class ProductEntityMapper(
    private val gson: Gson,
    private val assetManager: AssetManager
) :
    EntityMapper<MarketApiResponse, List<Product>> {

    private val productsImagesUrl = mapOf(
        Pair(
            "MUG",
            "https://cdn.shopify.com/s/files/1/0312/6537/products/27514-Black-White-1_aef806de-0299-4603-9305-bcc83155db8f_1024x1024.jpg?v=1495633232"
        ),
        Pair(
            "TSHIRT",
            "https://www.goalinn.com/f/13608/136088796/adidas-real-madrid-away-16-17.jpg"
        ),
        Pair("VOUCHER", "https://i.rafitamolin.com/18cfea7.png")
    )

    override fun mapFrom(inputModel: MarketApiResponse): List<Product> {

        val rawRules: List<DiscountRuleResponse?> = gson.fromJson<List<DiscountRuleResponse>>(
            assetManager.readJsonAssetFileName("productDiscountRules"),
            object : TypeToken<List<DiscountRuleResponse>>() {}.type
        )

        val rules = rawRules.map {
            when (it?.type) {
                "FreePerQuantityRule" -> FreePerQuantityDiscountRule(
                    it.code,
                    it.description.orEmpty(),
                    it.params?.freeQuantity ?: 0,
                    it.params?.buyQuantity ?: 0
                )
                "BulkRule" -> BulkDiscountRule(
                    it.code,
                    it.description.orEmpty(),
                    it.params?.bulkQuantity ?: 0,
                    it.params?.bulkPrice ?: 0.0
                )
                else -> null
            }
        }

        return inputModel.products?.map { productResponse ->
            val productParams = when (productResponse.id) {
                Product.Type.VOUCHER.typeId -> {
                    Triple(Product.Type.VOUCHER,
                        productsImagesUrl[Product.Type.VOUCHER.typeId],
                        rules.firstOrNull { it?.provideCode() == Product.Type.VOUCHER.typeId })
                }
                Product.Type.TSHIRT.typeId -> {
                    Triple(Product.Type.TSHIRT,
                        productsImagesUrl[Product.Type.TSHIRT.typeId].orEmpty(),
                        rules.firstOrNull { it?.provideCode() == Product.Type.TSHIRT.typeId })
                }
                Product.Type.MUG.typeId -> {
                    Triple(Product.Type.MUG,
                        productsImagesUrl[Product.Type.MUG.typeId].orEmpty(),
                        rules.firstOrNull { it?.provideCode() == Product.Type.MUG.typeId })
                }
                else -> Triple(
                    Product.Type.OTHER,
                    null, null
                )
            }

            Product(
                productParams.first,
                productResponse.name.orEmpty(),
                CurrencyAmount(productResponse.price),
                productParams.second.orEmpty(),
                productParams.third
            )
        } ?: emptyList()
    }

}