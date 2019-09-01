package com.dmanluc.cabifymarket.data.local.converter

import androidx.room.TypeConverter
import com.dmanluc.cabifymarket.domain.entity.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.KoinComponent
import org.koin.core.inject

class ShoppingCartConverter : KoinComponent {

    private val gson: Gson by inject()

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