package com.dmanluc.cabifymarket.data.remote.repository

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.remote.datasource.MarketRemoteDataSource
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import com.dmanluc.cabifymarket.utils.Resource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Implementation of market repository to obtain market products from API (if not saved in database or user forces refresh) or from database
 *
 */
class MarketRepositoryImpl(private val remoteDataSource: MarketRemoteDataSource) :
    MarketRepository {

    override suspend fun getProducts(forceRefresh: Boolean): LiveData<Resource<List<Product>>> {
        return remoteDataSource.getProducts(forceRefresh)
    }

}