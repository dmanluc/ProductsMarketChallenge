package com.dmanluc.cabifymarket.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dmanluc.cabifymarket.data.local.converter.ShoppingCartConverter
import com.dmanluc.cabifymarket.domain.entity.Product

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 */
@Entity(tableName = "shoppingCart")
@TypeConverters(ShoppingCartConverter::class)
data class ShoppingCartEntity(@ColumnInfo(name = "cart") var cart: LinkedHashMap<Product, Int>) {

    @PrimaryKey
    var _id: Int = 0

}