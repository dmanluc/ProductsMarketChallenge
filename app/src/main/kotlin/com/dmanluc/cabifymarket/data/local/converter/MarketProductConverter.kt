package com.dmanluc.cabifymarket.data.local.converter

import androidx.room.TypeConverter
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductDiscountRule
import com.google.gson.Gson
import org.koin.core.KoinComponent
import org.koin.core.inject

class MarketProductConverter : KoinComponent {

    private val gson: Gson by inject()

    @TypeConverter
    fun fromProductTypeEnumToString(type: Product.Type): String {
        return type.typeId
    }

    @TypeConverter
    fun fromStringToProductTypeEnum(typeId: String): Product.Type {
        return Product.Type.valueOf(typeId)
    }

    @TypeConverter
    fun fromCurrencyAmountToDouble(currencyAmount: CurrencyAmount): Double {
        return currencyAmount.amount
    }

    @TypeConverter
    fun fromDoubleToCurrencyAmount(amount: Double): CurrencyAmount {
        return CurrencyAmount(amount)
    }

    @TypeConverter
    fun fromDiscountRuleToString(rule: ProductDiscountRule?): String {
        return gson.toJson(rule, ProductDiscountRule::class.java)
    }

    @TypeConverter
    fun fromStringToDiscountRule(ruleStringInfo: String): ProductDiscountRule? {
        return gson.fromJson(ruleStringInfo, ProductDiscountRule::class.java)
    }

}