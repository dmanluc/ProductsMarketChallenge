package com.dmanluc.cabifymarket.domain.entity

/**
 * Interface which models the contract from a product discount rule
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
interface ProductDiscountRule {

    fun provideCode(): String

    fun provideDiscountInfo(): String

    fun calculateTotalProductPrice(quantity: Int, productPrice: Double): Double

}