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
import com.dmanluc.cabifymarket.utils.AppDispatchers
import com.dmanluc.cabifymarket.utils.Event
import com.dmanluc.cabifymarket.utils.notifyObserver
import com.dmanluc.cabifymarket.utils.observeAndMapValue
import com.dmanluc.cabifymarket.utils.safeLet
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class MarketProductsViewModel(
    private val getProductsInteractor: GetProductsInteractor,
    private val saveProductsCartInteractor: SaveProductsCartInteractor,
    private val getLastSavedProductsCartInteractor: GetLastSavedProductsCartInteractor,
    private val dispatchers: AppDispatchers
) : BaseViewModel() {

    private var _productsSource: LiveData<Resource<List<Product>>> = MutableLiveData()
    private val _products: MediatorLiveData<Resource<List<Product>>> = MediatorLiveData()
    val products: LiveData<Resource<List<Product>>>
        get() = _products

    private var _productsCartSource: LiveData<Resource<ProductsCart>> = MutableLiveData()
    private val _productsCart: MediatorLiveData<ProductsCart> = MediatorLiveData()
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
                    _snackbarErrorWithStringLiteral.value = Event(it.error?.message.orEmpty())
                }

                it
            }
        }
    }

    fun checkLastSavedProductsCart(productsFromMarketResource: Resource<List<Product>>) {
        if (productsFromMarketResource.status == Resource.Status.SUCCESS) {
            viewModelScope.launch(dispatchers.main) {
                _productsCartSource = getLastSavedProductsCartInteractor()
                _productsCart.observeAndMapValue(_productsCartSource) {
                    if (it.status == Resource.Status.ERROR) {
                        _snackbarErrorWithStringResId.value = Event(R.string.general_error_api_message)
                    }

                    safeLet(productsFromMarketResource.data, it.data) { first, second ->
                        filterValidProductsFromCart(first, second)
                    } ?: ProductsCart()
                }
            }
        }
    }

    private fun filterValidProductsFromCart(marketProducts: List<Product>, productsCart: ProductsCart): ProductsCart {
        if (productsCart.size() == 0) return productsCart

        val marketProductsId = marketProducts.map { it.type.typeId }

        val checking = productsCart.getProducts().keys.toList()
            .partition { it.type.typeId in marketProductsId }

        checking.second.forEach { productsCart.removeProduct(it) }

        return ProductsCart(productsCart.getProducts().mapKeys { keyEntry ->
            checking.first.first { it.type.typeId == keyEntry.key.type.typeId }
        } as LinkedHashMap<Product, Int>)
    }

    fun addProductToCart(quantity: Int, product: Product) {
        val cart = _productsCart.value ?: return

        cart.addProduct(quantity, product)

        _productsCart.notifyObserver()

        saveProductsCart(cart)
    }

    private fun saveProductsCart(cart: ProductsCart) {
        viewModelScope.launch(dispatchers.main) {
            saveProductsCartInteractor(cart)
        }
    }

    fun goToCheckout() {
        productsCart.value?.let {
            navigate(MarketProductsFragmentDirections.actionGoToCheckout(it))
        }
    }

}