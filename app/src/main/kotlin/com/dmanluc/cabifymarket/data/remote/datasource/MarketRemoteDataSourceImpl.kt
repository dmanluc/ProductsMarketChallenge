package com.dmanluc.cabifymarket.data.remote.datasource

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.local.dao.MarketProductsDao
import com.dmanluc.cabifymarket.data.local.mapper.MarketProductDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.local.mapper.ProductDomainToDatabaseEntityMapper
import com.dmanluc.cabifymarket.data.remote.api.MarketApi
import com.dmanluc.cabifymarket.data.remote.mapper.ProductEntityMapper
import com.dmanluc.cabifymarket.data.remote.model.MarketApiResponse
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.utils.Resource
import kotlinx.coroutines.Deferred

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Implementation of remote data source to fetch and save to database market products
 *
 */
class MarketRemoteDataSourceImpl(
    private val marketApi: MarketApi,
    private val dataToDomainEntityMapper: ProductEntityMapper,
    private val domainToDatabaseEntityMapper: ProductDomainToDatabaseEntityMapper,
    private val databaseToDomainEntityMapper: MarketProductDatabaseEntityToDomainMapper,
    private val localDao: MarketProductsDao
) : MarketRemoteDataSource {

    override suspend fun getProducts(forceRefresh: Boolean): LiveData<Resource<List<Product>>> {
        return object : NetworkBoundResource<List<Product>, MarketApiResponse>() {
            override suspend fun saveCallResults(items: List<Product>) {
                localDao.save(items.map { domainToDatabaseEntityMapper.mapFrom(it) })
            }

            override fun shouldFetch(data: List<Product>?): Boolean =
                data == null || data.isEmpty() || forceRefresh

            override suspend fun loadFromDb(): List<Product> {
                return localDao.getMarketProducts().map { databaseToDomainEntityMapper.mapFrom(it) }
            }

            override fun createCallAsync(): Deferred<MarketApiResponse> {
                return marketApi.getProductsAsync()
            }

            override fun processResponse(response: MarketApiResponse): List<Product> {
                return dataToDomainEntityMapper.mapFrom(response)
            }
        }.build().asLiveData()
    }

}