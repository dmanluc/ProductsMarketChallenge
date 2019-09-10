package com.dmanluc.cabifymarket.presentation.feature.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.domain.usecase.DeleteLocalProductsCartUseCase
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import com.dmanluc.cabifymarket.utils.Event
import com.dmanluc.cabifymarket.utils.notifyObserver
import com.dmanluc.cabifymarket.utils.postNotifyObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-20.
 */
class MarketCheckoutViewModel(
    private val deleteLocalProductsCartUseCase: DeleteLocalProductsCartUseCase
) : BaseViewModel() {

    private val _productsCart: MutableLiveData<ProductsCart> = MutableLiveData()
    val productsCart: LiveData<ProductsCart>
        get() = _productsCart

    private val _finishCheckoutFlow: MutableLiveData<Event<Unit>> = MutableLiveData()
    val finishCheckoutFlow: LiveData<Event<Unit>>
        get() = _finishCheckoutFlow

    fun loadCartProducts(cart: ProductsCart) {
        _productsCart.value = cart
    }

    fun updateProductCartQuantity(newQuantity: Int, product: Product) {
        _productsCart.value?.let {
            it.updateProduct(newQuantity, product)
            _productsCart.notifyObserver()
        }
    }

    fun removeProductFromCart(product: Product) {
        _productsCart.value?.let {
            it.removeProduct(product)
            _productsCart.notifyObserver()
        }
    }

    fun closeFlow() {
        _productsCart.value?.let {
            GlobalScope.launch {
                deleteLocalProductsCartUseCase.invoke(it)

                it.clearCart()
                _productsCart.postNotifyObserver()
            }
            _finishCheckoutFlow.value = Event(Unit)
        }
    }

}