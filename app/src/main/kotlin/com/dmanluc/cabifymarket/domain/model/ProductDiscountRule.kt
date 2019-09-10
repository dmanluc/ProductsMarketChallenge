package com.dmanluc.cabifymarket.domain.model

import java.io.Serializable

/**
 * Interface which models the contract from a product discount rule
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
interface ProductDiscountRule : Serializable {

    fun provideCode(): String

    fun provideDiscountInfo(): String

    fun calculateProductPriceWithDiscount(quantity: Int, productPrice: Double): Double

    fun calculateProductsTotalPrice(quantity: Int, productPrice: Double): Double

}