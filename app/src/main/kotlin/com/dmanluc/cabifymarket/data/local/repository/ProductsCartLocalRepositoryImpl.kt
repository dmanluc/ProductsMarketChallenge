package com.dmanluc.cabifymarket.data.local.repository

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.local.datasource.ProductsCartLocalDataSource
import com.dmanluc.cabifymarket.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository

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

    override suspend fun saveProductsCart(cart: ProductsCart) {
        localDataSource.saveProductsCart(cart)
    }

    override suspend fun getLastSavedProductsCart(): LiveData<Resource<ProductsCart>> {
        return localDataSource.getLastSavedProductsCart()
    }

    override suspend fun deleteProductsCart(productsCart: ProductsCart) {
        localDataSource.deleteProductsCart(productsCart)
    }

}