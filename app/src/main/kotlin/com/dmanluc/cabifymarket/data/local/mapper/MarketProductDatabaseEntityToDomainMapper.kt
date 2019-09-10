package com.dmanluc.cabifymarket.data.local.mapper

import com.dmanluc.cabifymarket.data.local.model.MarketProductEntity
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.utils.EntityMapper

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-31.
 *
 * Mapper to convert between database product entity and domain product entity
 *
 */
class MarketProductDatabaseEntityToDomainMapper : EntityMapper<MarketProductEntity, Product> {

    override fun mapFrom(inputModel: MarketProductEntity): Product {
        return inputModel.let {
            Product(it.type, it.name, it.price, it.imageUrl, it.discountRule)
        }
    }

}