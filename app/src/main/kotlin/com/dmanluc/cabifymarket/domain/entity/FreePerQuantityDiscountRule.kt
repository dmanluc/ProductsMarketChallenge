package com.dmanluc.cabifymarket.domain.entity

/**
 * Discount rule for a free per quantity discount (e.g. 2x1)
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class FreePerQuantityDiscountRule(
    private val code: String,
    private val discountDescription: String,
    private val freeQuantity: Int,
    private val buyQuantity: Int
) : ProductDiscountRule {

    override fun provideCode(): String {
        return code
    }

    override fun provideDiscountInfo(): String {
        return discountDescription
    }

    override fun calculateTotalProductPrice(quantity: Int, productPrice: Double): Double {
        val productSetsWithDiscount = quantity / freeQuantity

        val productsWithDiscount = productSetsWithDiscount * buyQuantity
        val productsWithoutDiscount = quantity % freeQuantity

        return (productsWithDiscount + productsWithoutDiscount) * productPrice
    }

}