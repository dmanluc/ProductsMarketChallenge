package com.dmanluc.cabifymarket.data.local.repository

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSource
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository
import com.dmanluc.cabifymarket.utils.Resource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 *
 * Implementation of local products cart repository to obtain/save/delete saved user products cart from local app database
 *
 */
class ProductsCartLocalRepositoryImpl(private val localDataSource: ProductsCartLocalDataSource) :
    ProductsCartLocalRepository {

    override suspend fun saveLocalProductsCart(cart: ProductsCart) {
        localDataSource.saveLocalProductsCart(cart)
    }

    override suspend fun getLocalProductsCart(): LiveData<Resource<ProductsCart>> {
        return localDataSource.getLocalProductsCart()
    }

    override suspend fun deleteLocalProductsCart(productsCart: ProductsCart) {
        localDataSource.deleteLocalProductsCart(productsCart)
    }

}