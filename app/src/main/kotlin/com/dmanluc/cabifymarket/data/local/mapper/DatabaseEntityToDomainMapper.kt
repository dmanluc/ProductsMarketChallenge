package com.dmanluc.cabifymarket.data.local.mapper

import com.dmanluc.cabifymarket.data.local.model.ShoppingCartEntity
import com.dmanluc.cabifymarket.domain.entity.ProductsCart
import utils.EntityMapper

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 */
class DatabaseEntityToDomainMapper: EntityMapper<ShoppingCartEntity, ProductsCart> {

    override fun mapFrom(inputModel: ShoppingCartEntity): ProductsCart {
        return ProductsCart(inputModel.cart)
    }

}