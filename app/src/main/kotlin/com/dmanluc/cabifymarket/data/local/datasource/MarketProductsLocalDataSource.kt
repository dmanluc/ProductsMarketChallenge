package com.dmanluc.cabifymarket.data.local.datasource

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
interface MarketProductsLocalDataSource {

    suspend fun getProducts(): LiveData<Resource<List<Product>>>

}