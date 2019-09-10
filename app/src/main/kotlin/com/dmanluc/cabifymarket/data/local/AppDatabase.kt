package com.dmanluc.cabifymarket.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dmanluc.cabifymarket.data.local.dao.MarketProductsDao
import com.dmanluc.cabifymarket.data.local.dao.ShoppingCartDao
import com.dmanluc.cabifymarket.data.local.model.MarketProductEntity
import com.dmanluc.cabifymarket.data.local.model.ShoppingCartEntity

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 *
 * Room app database declaration
 *
 */
@Database(
    entities = [MarketProductEntity::class, ShoppingCartEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoppingCartDao(): ShoppingCartDao

    abstract fun marketProductsDao(): MarketProductsDao

    companion object {
        fun buildDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "MarketProducts.db"
        ).build()
    }

}