package com.dmanluc.cabifymarket.domain.usecase

import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Use case to delete user products cart in database
 *
 */
class DeleteLocalProductsCartUseCase(private val repository: ProductsCartLocalRepository) {

    suspend operator fun invoke(cart: ProductsCart) {
        return repository.deleteLocalProductsCart(cart)
    }

}