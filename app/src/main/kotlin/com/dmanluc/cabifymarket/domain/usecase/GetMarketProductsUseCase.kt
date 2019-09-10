package com.dmanluc.cabifymarket.domain.usecase

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.repository.MarketRepository
import com.dmanluc.cabifymarket.utils.Resource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Use case to fetch market products from remote data source (if not saved in database or user forces refresh) and save them to database.
   If saved, return them from database.
 *
 */
class GetMarketProductsUseCase(private val repository: MarketRepository) {

    suspend operator fun invoke(forceRefresh: Boolean = false): LiveData<Resource<List<Product>>> {
        return repository.getProducts(forceRefresh)
    }

}