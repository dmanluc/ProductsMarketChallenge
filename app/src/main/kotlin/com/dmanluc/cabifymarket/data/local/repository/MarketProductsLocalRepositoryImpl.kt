package com.dmanluc.cabifymarket.data.local.repository

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.local.datasource.MarketProductsLocalDataSource
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.repository.MarketProductsLocalRepository

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 *
 * Implementation of local market products repository to obtain saved products from local app database
 *
 */
class MarketProductsLocalRepositoryImpl(private val localDataSource: MarketProductsLocalDataSource) :
    MarketProductsLocalRepository {

    override suspend fun getProducts(): LiveData<Resource<List<Product>>> {
        return localDataSource.getProducts()
    }

}