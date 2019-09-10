package com.dmanluc.cabifymarket.data.local.mapper

import com.dmanluc.cabifymarket.data.local.model.ShoppingCartEntity
import com.dmanluc.cabifymarket.domain.entity.Product
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import com.dmanluc.cabifymarket.utils.EntityMapper

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 *
 * Mapper to convert between domain products cart entity and database shopping cart entity
 *
 */
class ProductsCartDomainToDatabaseEntityMapper : EntityMapper<ProductsCart, ShoppingCartEntity> {

    override fun mapFrom(inputModel: ProductsCart): ShoppingCartEntity {
        return ShoppingCartEntity(inputModel.getProducts().toMutableMap() as LinkedHashMap<Product, Int>)
    }

}