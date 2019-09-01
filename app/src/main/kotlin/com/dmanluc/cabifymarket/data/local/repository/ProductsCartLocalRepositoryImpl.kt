package com.dmanluc.cabifymarket.data.local.repository

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSource
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 */
class ProductsCartLocalRepositoryImpl(private val localDataSource: ProductsCartLocalDataSource) :
    ProductsCartLocalRepository {

    override suspend fun saveProductsCart(cart: ProductsCart) {
        localDataSource.saveProductsCart(cart)
    }

    override suspend fun getLastSavedProductsCart(): LiveData<Resource<ProductsCart>> {
        return localDataSource.getLastSavedProductsCart()
    }

}