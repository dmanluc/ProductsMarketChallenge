package com.dmanluc.cabifymarket.domain.entity

/**
 * Discount rule for a bulk quantity discount (e.g. starting 3 or more product at X €)
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class BulkDiscountRule(private val code: String,
                       private val discountDescription: String,
                       private val buyQuantity: Int,
                       private val priceWithDiscount: Double): ProductDiscountRule {

    override fun provideCode(): String {
        return code
    }

    override fun provideDiscountInfo(): String {
        return discountDescription
    }

    override fun calculateTotalProductPrice(quantity: Int, productPrice: Double): Double {
        return if (quantity < buyQuantity) quantity * productPrice else quantity * priceWithDiscount
    }

}