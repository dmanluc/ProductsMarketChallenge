package com.dmanluc.cabifymarket.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.dmanluc.cabifymarket.data.local.entity.MarketProductEntity

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-31.
 *
 * Custom DAO for inserting and saving market products
 *
 */
@Dao
abstract class MarketProductsDao : BaseDao<MarketProductEntity>() {

    @Query("SELECT * FROM marketProducts ORDER BY name")
    abstract suspend fun getMarketProducts(): List<MarketProductEntity>

    suspend fun save(products: List<MarketProductEntity>) {
        insert(products)
    }

}