package com.dmanluc.cabifymarket.domain.interactor

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.repository.MarketRepository

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class GetProductsInteractor(private val repository: MarketRepository) {

    suspend operator fun invoke(forceRefresh: Boolean = false): LiveData<Resource<List<Product>>> {
        return repository.getProducts(forceRefresh)
    }

}