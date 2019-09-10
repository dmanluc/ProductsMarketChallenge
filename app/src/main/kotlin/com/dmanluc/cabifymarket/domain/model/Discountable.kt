package com.dmanluc.cabifymarket.domain.model

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

    fun providePriceWithDiscount(productQuantity: Int): CurrencyAmount

    fun provideTotalPrice(productQuantity: Int): CurrencyAmount

}