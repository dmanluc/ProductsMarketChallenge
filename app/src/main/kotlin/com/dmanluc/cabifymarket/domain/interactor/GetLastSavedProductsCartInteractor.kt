package com.dmanluc.cabifymarket.domain.interactor

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class GetLastSavedProductsCartInteractor(private val repository: ProductsCartLocalRepository) {

    suspend operator fun invoke(): LiveData<Resource<ProductsCart>> {
        return repository.getLastSavedProductsCart()
    }

}