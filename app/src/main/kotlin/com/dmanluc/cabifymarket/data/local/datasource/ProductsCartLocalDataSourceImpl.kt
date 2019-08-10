package com.dmanluc.cabifymarket.data.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.data.local.dao.ShoppingCartDao
import com.dmanluc.cabifymarket.data.local.mapper.DatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.data.local.mapper.DomainToDatabaseEntityMapper
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class ProductsCartLocalDataSourceImpl(
    private val dao: ShoppingCartDao,
    private val domainToEntityMapper: DomainToDatabaseEntityMapper,
    private val databaseEntityToDomainMapper: DatabaseEntityToDomainMapper
) : ProductsCartLocalDataSource {

    private val result = MutableLiveData<Resource<ProductsCart>>()

    override suspend fun saveProductsCart(cart: ProductsCart) {
        CoroutineScope(coroutineContext).launch {
            dao.saveShoppingCart(domainToEntityMapper.mapFrom(cart))
        }
    }

    override suspend fun getLastSavedProductsCart(): LiveData<Resource<ProductsCart>> {
        CoroutineScope(coroutineContext).launch {
            val newValue = Resource.Success(databaseEntityToDomainMapper.mapFrom(dao.getShoppingCart()))
            if (result.value != newValue) result.postValue(newValue)
        }

        return result
    }

}