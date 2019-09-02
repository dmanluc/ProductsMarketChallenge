package com.dmanluc.cabifymarket.presentation.feature.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.domain.interactor.GetProductsInteractor
import com.dmanluc.cabifymarket.domain.interactor.SaveProductsCartInteractor
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import com.dmanluc.cabifymarket.utils.AppDispatchers
import com.dmanluc.cabifymarket.utils.Event
import com.dmanluc.cabifymarket.utils.notifyObserver
import com.dmanluc.cabifymarket.utils.observeAndMapValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketProductsViewModel(private val getProductsInteractor: GetProductsInteractor,
                              private val saveProductsCartInteractor: SaveProductsCartInteractor,
                              private val dispatchers: AppDispatchers) : BaseViewModel() {

    private var _productsSource: LiveData<Resource<List<Product>>> = MutableLiveData()
    private val _products: MediatorLiveData<Resource<List<Product>>> = MediatorLiveData()
    val products: LiveData<Resource<List<Product>>>
        get() = _products

    private val _productsCart: MutableLiveData<ProductsCart> = MutableLiveData(ProductsCart())
    val productsCart: LiveData<ProductsCart>
        get() = _productsCart

    init {
        fetchMarketProducts()
    }

    fun refreshMarketProducts() {
        fetchMarketProducts(forceRefresh = true)
    }

    private fun fetchMarketProducts(forceRefresh: Boolean = false) {
        viewModelScope.launch(dispatchers.main) {
            withContext(dispatchers.io) {
                _productsSource = getProductsInteractor(forceRefresh)
            }
            _products.observeAndMapValue(_productsSource) {
                if (it.status == Resource.Status.ERROR) {
                    _snackbarError.value = Event(R.string.general_error_api_message)
                }
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
            viewModelScope.launch(dispatchers.main) {
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