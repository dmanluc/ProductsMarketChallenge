package com.dmanluc.cabifymarket.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmanluc.cabifymarket.data.local.converter.ShoppingCartConverter
import com.dmanluc.cabifymarket.data.local.dao.ShoppingCartDao
import com.dmanluc.cabifymarket.data.local.model.ShoppingCartEntity

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 */
@Database(entities = [ShoppingCartEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shoppingCartDao(): ShoppingCartDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "AppProductsCart.db"
            ).build()
    }

}