package com.dmanluc.cabifymarket.domain.entity

import java.io.Serializable

/**
 * Domain model entity for Cabify market product. Implements Discountable interface that provides some discount info related to it.
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
data class Product(
    val type: Type = Type.OTHER,
    val name: String,
    val currencyAmount: CurrencyAmount,
    val imageUrl: String?,
    val discountRule: ProductDiscountRule?
) : Discountable, Serializable {

    enum class Type(val typeId: String) {
        VOUCHER("VOUCHER"),
        TSHIRT("TSHIRT"),
        MUG("MUG"),
        OTHER("")
    }

    override fun hasDiscount(): Boolean {
        return discountRule != null
    }

    override fun provideDiscountInfo(): String {
        return discountRule?.provideDiscountInfo().orEmpty()
    }

    override fun provideTotalPrice(productQuantity: Int): CurrencyAmount {
        return CurrencyAmount(
            discountRule?.calculateTotalProductPrice(
                productQuantity,
                currencyAmount.amount
            ) ?: run { productQuantity * currencyAmount.amount })
    }

}

