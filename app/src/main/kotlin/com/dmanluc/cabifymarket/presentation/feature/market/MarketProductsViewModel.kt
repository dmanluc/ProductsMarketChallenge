package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
import utils.notifyObserver
import utils.observeAndMapValue

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

    private var _productsSource: LiveData<Resource<List<Product>>> = MutableLiveData()
    private val _products: MediatorLiveData<Resource<List<Product>>> = MediatorLiveData()
    val products: LiveData<Resource<List<Product>>>
        get() = _products

    private val _productsCart: MutableLiveData<ProductsCart> = MutableLiveData(ProductsCart())
    val productsCart: LiveData<ProductsCart>
        get() = _productsCart

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _productsSource = getProductsInteractor()
            }
            _products.observeAndMapValue(_productsSource) {
                it
            }
        }
    }

    fun addProductToCart(quantity: Int, product: Product) {
        with(_productsCart) {
            value?.addProduct(quantity, product)
            notifyObserver()
        }
        saveProductsCart()
    }

    private fun saveProductsCart() {
        _productsCart.value?.let {
            viewModelScope.launch {
                saveProductsCartInteractor(it)
            }
        }
    }

    fun goToCheckout() {
        productsCart.value?.let {
            navigate(MarketProductsFragmentDirections.actionGoToCheckout(it))
        }
    }

}