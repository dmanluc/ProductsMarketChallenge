package com.dmanluc.cabifymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dmanluc.cabifymarket.data.local.entity.ShoppingCartEntity

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * Custom DAO for inserting and saving current user products cart
 *
 */
@Dao
abstract class ShoppingCartDao : BaseDao<ShoppingCartEntity>() {

    @Query("SELECT * FROM shoppingCart LIMIT 1")
    abstract suspend fun getShoppingCart(): ShoppingCartEntity

    suspend fun saveShoppingCart(cart: ShoppingCartEntity) {
        insert(cart)
    }

    suspend fun deleteShoppingCart(cart: ShoppingCartEntity) {
        delete(cart)
    }

}