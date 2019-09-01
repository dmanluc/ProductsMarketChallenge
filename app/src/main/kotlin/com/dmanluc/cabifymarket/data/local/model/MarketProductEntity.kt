package com.dmanluc.cabifymarket.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dmanluc.cabifymarket.data.local.converter.MarketProductConverter
import com.dmanluc.cabifymarket.domain.entity.CurrencyAmount
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductDiscountRule

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-31.
 */
@Entity(tableName = "marketProducts")
@TypeConverters(MarketProductConverter::class)
data class MarketProductEntity(@PrimaryKey val type: Product.Type, val name: String,
                               val price: CurrencyAmount,
                               val imageUrl: String?,
                               val discountRule: ProductDiscountRule?)