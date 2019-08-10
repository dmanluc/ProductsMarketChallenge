package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.interactor.GetLastSavedProductsCartInteractor
import com.dmanluc.cabifymarket.domain.interactor.GetProductsInteractor
import com.dmanluc.cabifymarket.domain.interactor.SaveProductsCartInteractor
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import utils.Event
import utils.notifyObserver

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketProductsViewModel(
    private val getProductsInteractor: GetProductsInteractor,
    private val saveProductsCartInteractor: SaveProductsCartInteractor,
    private val getLastSavedProductsCartInteractor: GetLastSavedProductsCartInteractor
) : BaseViewModel() {

    private val products: MediatorLiveData<Resource<List<Product>>> = MediatorLiveData()
    private var productsSource: LiveData<Resource<List<Product>>> = MutableLiveData()

    private val productsCart = MutableLiveData<ProductsCart>().apply { value = ProductsCart() }

    fun getProducts(): LiveData<Resource<List<Product>>> = products

    fun fetchProducts() {
        products.removeSource(productsSource)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productsSource = getProductsInteractor()
            }
            products.addSource(productsSource) { resource ->
                products.value = resource

                when (resource) {
                    is Resource.Error -> {
                        _snackbarError.value = Event(R.string.general_error_message)
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun printLastSavedProductsCart() {
        viewModelScope.launch(Dispatchers.Default) {
            val returnData = getLastSavedProductsCartInteractor()
            withContext(Dispatchers.Main) {
                returnData.observeForever {
                    println(it.data)
                }
            }
        }
    }

    fun getProductsCart() = productsCart

    fun addProductToCart(quantity: Int, product: Product) {
        with(productsCart) {
            value?.addProduct(quantity, product)
            notifyObserver()
            saveProductsCart()
        }
    }

    private fun saveProductsCart() {
        productsCart.value?.let {
            viewModelScope.launch(Dispatchers.Default) {
                saveProductsCartInteractor(it)
            }
        }
    }

}