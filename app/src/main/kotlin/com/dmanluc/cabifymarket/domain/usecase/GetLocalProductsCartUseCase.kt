package com.dmanluc.cabifymarket.domain.usecase

import androidx.lifecycle.LiveData
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.domain.repository.ProductsCartLocalRepository
import com.dmanluc.cabifymarket.utils.Resource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 * Use case to get saved user products cart in database
 *
 */
class GetLocalProductsCartUseCase(private val repository: ProductsCartLocalRepository) {

    suspend operator fun invoke(): LiveData<Resource<ProductsCart>> {
        return repository.getLocalProductsCart()
    }

}