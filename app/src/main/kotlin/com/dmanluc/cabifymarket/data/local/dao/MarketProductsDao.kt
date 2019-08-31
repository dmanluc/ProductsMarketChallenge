package com.dmanluc.cabifymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dmanluc.cabifymarket.data.local.model.MarketProductEntity

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-31.
 */
@Dao
abstract class MarketProductsDao : BaseDao<MarketProductEntity>() {

    @Query("SELECT * FROM marketProducts ORDER BY _id")
    abstract suspend fun getMarketProducts(): List<MarketProductEntity>

    suspend fun save(products: List<MarketProductEntity>) {
        insert(products)
    }

}