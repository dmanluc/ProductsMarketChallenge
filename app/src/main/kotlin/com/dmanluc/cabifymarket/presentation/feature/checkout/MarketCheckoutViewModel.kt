package com.dmanluc.cabifymarket.presentation.feature.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.presentation.base.BaseViewModel

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

}