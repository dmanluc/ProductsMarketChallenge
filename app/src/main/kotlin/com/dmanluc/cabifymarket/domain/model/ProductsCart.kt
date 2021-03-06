package com.dmanluc.cabifymarket.domain.model

import java.io.Serializable

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-03.
 *
 * Domain model that represents the products (and its quantity) that the user added to the shopping cart
 *
 */
data class ProductsCart(private val cart: LinkedHashMap<Product, Int> = linkedMapOf()) : Serializable {

    fun addProduct(quantity: Int, productToAdd: Product) {
        cart[productToAdd] = (cart[productToAdd] ?: 0) + quantity
    }

    fun removeProduct(productToRemove: Product) {
        cart.remove(productToRemove)
    }

    fun updateProduct(newQuantity: Int, product: Product) {
        if (cart.containsKey(product) && newQuantity > 0) {
            cart[product] = newQuantity
        }
    }

    fun size(): Int = cart.map { it.value }.sum()

    fun getTotalAmount(): Double {
        return cart.map { mapEntry ->
            mapEntry.key.provideTotalPrice(mapEntry.value).amount
        }.sumByDouble { it }
    }

    fun getProducts(): Map<Product, Int> {
        return cart
    }

    fun clearCart() {
        cart.clear()
    }

}