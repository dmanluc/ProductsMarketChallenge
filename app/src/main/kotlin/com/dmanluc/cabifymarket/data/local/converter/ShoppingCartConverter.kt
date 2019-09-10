package com.dmanluc.cabifymarket.data.local.converter

import androidx.room.TypeConverter
import com.dmanluc.cabifymarket.domain.model.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * Specific Room type converter for domain products cart entity to be able to get it and save it from/to database
 *
 */
class ShoppingCartConverter : KoinComponent {

    private val gson: Gson by inject(named("shoppingCartDatabaseConverterGson"))

    @TypeConverter
    fun fromStringToMap(value: String): LinkedHashMap<Product, Int> {
        val mapType = object : TypeToken<LinkedHashMap<Product, Int>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMapToString(map: LinkedHashMap<Product, Int>): String {
        return gson.toJson(map)
    }

}