package com.dmanluc.cabifymarket.data.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.data.local.dao.MarketProductsDao
import com.dmanluc.cabifymarket.data.local.mapper.MarketProductDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * Implementation of a local data source which obtains market products from local app database
 *
 */
class MarketProductsLocalDataSourceImpl(
    private val dao: MarketProductsDao,
    private val databaseToDomainMapper: MarketProductDatabaseEntityToDomainMapper,
    cacheDataSource: CacheDataSource
) : MarketProductsLocalDataSource {

    private val result = MutableLiveData<Resource<List<Product>>>()

    init {
        cacheDataSource.manageExpiration(enable = false)
    }

    override suspend fun getLocalProducts(): LiveData<Resource<List<Product>>> {
        CoroutineScope(coroutineContext).launch {
            try {
                val newValue =
                    Resource.success(dao.getMarketProducts().map { databaseToDomainMapper.mapFrom(it) })
                if (result.value != newValue) result.postValue(newValue)
            } catch (exception: Exception) {
                result.postValue(Resource.error(exception))
            }
        }

        return result
    }

}