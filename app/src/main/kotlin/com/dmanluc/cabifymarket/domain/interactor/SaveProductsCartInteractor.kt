package com.dmanluc.cabifymarket.domain.interactor

import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartRepository

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class SaveProductsCartInteractor(private val repository: ProductsCartRepository) {

    suspend operator fun invoke(cart: ProductsCart) {
        return repository.saveProductsCart(cart)
    }

}