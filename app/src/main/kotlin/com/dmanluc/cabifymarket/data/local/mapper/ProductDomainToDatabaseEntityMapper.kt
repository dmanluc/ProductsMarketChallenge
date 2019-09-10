package com.dmanluc.cabifymarket.data.local.mapper

import com.dmanluc.cabifymarket.data.local.entity.MarketProductEntity
import com.dmanluc.cabifymarket.domain.model.Product
import com.dmanluc.cabifymarket.utils.EntityMapper

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-31.
 *
 * Mapper to convert between domain product entity and database product entity
 *
 */
class ProductDomainToDatabaseEntityMapper : EntityMapper<Product, MarketProductEntity> {

    override fun mapFrom(inputModel: Product): MarketProductEntity {
        return inputModel.let {
            MarketProductEntity(it.type, it.name, it.price, it.imageUrl, it.discountRule)
        }
    }

}