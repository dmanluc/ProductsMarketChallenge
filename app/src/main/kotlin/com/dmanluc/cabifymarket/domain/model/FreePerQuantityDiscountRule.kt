package com.dmanluc.cabifymarket.domain.model

/**
 * Domain model of a discount rule for a free per quantity discount (e.g. 2x1)
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class FreePerQuantityDiscountRule(
    private val code: String,
    private val discountDescription: String,
    val freeQuantity: Int,
    val buyQuantity: Int
) : ProductDiscountRule {

    override fun provideCode(): String {
        return code
    }

    override fun provideDiscountInfo(): String {
        return discountDescription
    }

    override fun calculateProductPriceWithDiscount(quantity: Int, productPrice: Double): Double {
        return calculateProductsTotalPrice(quantity, productPrice) / quantity
    }

    override fun calculateProductsTotalPrice(quantity: Int, productPrice: Double): Double {
        return if (freeQuantity > buyQuantity) {
            val productSetsWithDiscount = quantity / freeQuantity

            val productsWithDiscount = productSetsWithDiscount * buyQuantity
            val productsWithoutDiscount = quantity % freeQuantity

            (productsWithDiscount + productsWithoutDiscount) * productPrice
        } else {
            0.0
        }
    }

}