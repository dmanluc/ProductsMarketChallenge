package com.dmanluc.cabifymarket.data.remote.datasource

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.remote.api.MarketApi
import com.dmanluc.cabifymarket.data.remote.mapper.ProductEntityMapper
import com.dmanluc.cabifymarket.data.remote.model.MarketApiResponse
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import kotlinx.coroutines.Deferred

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketRemoteDataSourceImpl constructor(
    private val marketApi: MarketApi,
    private val entityMapper: ProductEntityMapper
) : MarketRemoteDataSource {

    override suspend fun getProducts(): LiveData<Resource<List<Product>>> {
        return object : RemoteBoundResource<List<Product>, MarketApiResponse>() {
            override fun processResponse(response: MarketApiResponse): List<Product> {
                return entityMapper.mapFrom(response)
            }

            override fun performNetworkCallAsync(): Deferred<MarketApiResponse> {
                return marketApi.getProductsAsync()
            }
        }.build().asLiveData()
    }

}