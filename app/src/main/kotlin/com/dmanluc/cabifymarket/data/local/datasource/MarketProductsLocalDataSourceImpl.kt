package com.dmanluc.cabifymarket.data.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.data.local.dao.MarketProductsDao
import com.dmanluc.cabifymarket.data.local.mapper.MarketProductDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class MarketProductsLocalDataSourceImpl(private val dao: MarketProductsDao,
                                        private val databaseToDomainMapper: MarketProductDatabaseEntityToDomainMapper,
                                        cacheDataSource: CacheDataSource) :
    MarketProductsLocalDataSource {

    private val result = MutableLiveData<Resource<List<Product>>>()

    init {
        cacheDataSource.manageExpiration(enable = false)
    }

    override suspend fun getProducts(): LiveData<Resource<List<Product>>> {
        CoroutineScope(coroutineContext).launch {
            val newValue =
                Resource.success(dao.getMarketProducts().map { databaseToDomainMapper.mapFrom(it) })
            if (result.value != newValue) result.postValue(newValue)
        }

        return result
    }

}