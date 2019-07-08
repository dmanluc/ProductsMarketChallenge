package com.dmanluc.cabifymarket.domain.entity

/**
 * Interface that could be implemented by a market product to be able to have a discount
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
interface Discountable {

    fun hasDiscount(): Boolean

    fun provideDiscountInfo(): String

    fun provideTotalPrice(productQuantity: Int): CurrencyAmount

}