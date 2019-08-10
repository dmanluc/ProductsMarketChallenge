package com.dmanluc.cabifymarket.domain.entity

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-03.
 */
data class ProductsCart(private val cart: HashMap<Product, Int> = linkedMapOf()) {

    fun addProduct(quantity: Int, productToAdd: Product) {
        cart[productToAdd] = (cart[productToAdd] ?: 0) + quantity
    }

    fun removeProduct(productToRemove: Product) {
        cart.remove(productToRemove)
    }

    fun size() = cart.map { it.value }.sum()

    fun getTotalAmount(): Double {
        return cart.map { mapEntry ->
            mapEntry.key.provideTotalPrice(mapEntry.value).amount
        }.sumByDouble { it }
    }

    fun getProducts(): Map<Product, Int> {
        return cart
    }

}