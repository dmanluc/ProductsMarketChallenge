package com.dmanluc.cabifymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dmanluc.cabifymarket.data.local.model.ShoppingCartEntity

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