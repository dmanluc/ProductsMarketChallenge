package com.dmanluc.cabifymarket.data.local.converter

import androidx.room.TypeConverter
import com.dmanluc.cabifymarket.domain.model.CurrencyAmount
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.domain.model.ProductDiscountRule
import com.google.gson.Gson
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * Specific Room type converter for domain product entity to be able to get it and save it from/to database
 *
 */
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