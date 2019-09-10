package com.dmanluc.cabifymarket.data.remote.repository

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.remote.datasource.MarketRemoteDataSource
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import com.dmanluc.cabifymarket.utils.Resource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Implementation of market repository to obtain remote products
 *
 */
class MarketRepositoryImpl(private val remoteDataSource: MarketRemoteDataSource) :
    MarketRepository {

    override suspend fun getProducts(forceRefresh: Boolean): LiveData<Resource<List<Product>>> {
        return remoteDataSource.getProducts(forceRefresh)
    }

}