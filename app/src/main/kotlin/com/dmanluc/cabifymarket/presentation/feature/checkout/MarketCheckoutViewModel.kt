package com.dmanluc.cabifymarket.presentation.feature.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel
import com.dmanluc.cabifymarket.utils.notifyObserver

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-20.
 */
class MarketCheckoutViewModel : BaseViewModel() {

    private val _productsCart: MutableLiveData<ProductsCart> = MutableLiveData()
    val productsCart: LiveData<ProductsCart>
        get() = _productsCart

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

}