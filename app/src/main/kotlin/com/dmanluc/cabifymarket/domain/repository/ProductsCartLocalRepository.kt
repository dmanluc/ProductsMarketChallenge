package com.dmanluc.cabifymarket.domain.repository

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.utils.Resource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 */
interface ProductsCartLocalRepository {

    suspend fun saveLocalProductsCart(cart: ProductsCart)

    suspend fun getLocalProductsCart(): LiveData<Resource<ProductsCart>>

    suspend fun deleteLocalProductsCart(productsCart: ProductsCart)

}