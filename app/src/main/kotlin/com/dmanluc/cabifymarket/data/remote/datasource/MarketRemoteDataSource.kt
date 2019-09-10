package com.dmanluc.cabifymarket.data.remote.datasource

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.utils.Resource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
interface MarketRemoteDataSource {

    suspend fun getProducts(forceRefresh: Boolean): LiveData<Resource<List<Product>>>

}