package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.interactor.GetProductsInteractor
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketProductsViewModel(private val getProductsInteractor: GetProductsInteractor) : BaseViewModel() {

    private val products: MediatorLiveData<Resource<List<Product>>> = MediatorLiveData()
    private var productsSource: LiveData<Resource<List<Product>>> = MutableLiveData()

    fun getProducts(): LiveData<Resource<List<Product>>> = products

    fun fetchProducts() {
        products.removeSource(productsSource)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productsSource = getProductsInteractor()
            }
            products.addSource(productsSource) {
                products.value = it
            }
        }
    }

}