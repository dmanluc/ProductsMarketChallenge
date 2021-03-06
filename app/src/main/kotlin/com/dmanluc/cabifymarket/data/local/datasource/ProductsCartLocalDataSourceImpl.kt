package com.dmanluc.cabifymarket.data.local.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.data.local.dao.ShoppingCartDao
import com.dmanluc.cabifymarket.data.local.mapper.ProductsCartDomainToDatabaseEntityMapper
import com.dmanluc.cabifymarket.data.local.mapper.ShoppingCartDatabaseEntityToDomainMapper
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Implementation of a local data source which save/obtains current user products cart to/from local app database
 *
 */
class ProductsCartLocalDataSourceImpl(
    private val dao: ShoppingCartDao,
    private val productsCartDomainToEntityMapper: ProductsCartDomainToDatabaseEntityMapper,
    private val shoppingCartDatabaseEntityToDomainMapper: ShoppingCartDatabaseEntityToDomainMapper,
    private val cacheDataSource: CacheDataSource
) :
    ProductsCartLocalDataSource {

    private val result = MutableLiveData<Resource<ProductsCart>>()

    init {
        cacheDataSource.manageExpiration(enable = false)
    }

    override suspend fun saveLocalProductsCart(cart: ProductsCart) {
        cacheDataSource.save(cart)

        CoroutineScope(coroutineContext).launch {
            dao.saveShoppingCart(productsCartDomainToEntityMapper.mapFrom(cart))
        }
    }

    override suspend fun getLocalProductsCart(): LiveData<Resource<ProductsCart>> {
        val cachedCart = cacheDataSource.get(ProductsCart::class.java)
        if (cachedCart != null) {
            val newValue = Resource.success(cachedCart)
            if (result.value != newValue) result.postValue(newValue)
            return result
        }

        CoroutineScope(coroutineContext).launch {
            try {
                val newValue =
                    Resource.success(shoppingCartDatabaseEntityToDomainMapper.mapFrom(dao.getShoppingCart()))
                if (result.value != newValue) result.postValue(newValue)
            } catch (exception: Exception) {
                result.postValue(Resource.error(exception))
            }
        }

        return result
    }

    override suspend fun deleteLocalProductsCart(productsCart: ProductsCart) {
        cacheDataSource.clear()

        CoroutineScope(coroutineContext).launch {
            dao.deleteShoppingCart(productsCartDomainToEntityMapper.mapFrom(productsCart))
        }
    }

}