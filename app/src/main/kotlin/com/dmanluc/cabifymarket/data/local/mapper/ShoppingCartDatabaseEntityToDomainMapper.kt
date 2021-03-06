package com.dmanluc.cabifymarket.data.local.mapper

import com.dmanluc.cabifymarket.data.local.entity.ShoppingCartEntity
import com.dmanluc.cabifymarket.domain.model.ProductsCart
import com.dmanluc.cabifymarket.utils.EntityMapper

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-09.
 *
 * Mapper to convert between database shopping cart entity and domain products cart entity
 *
 */
class ShoppingCartDatabaseEntityToDomainMapper : EntityMapper<ShoppingCartEntity?, ProductsCart?> {

    override fun mapFrom(inputModel: ShoppingCartEntity?): ProductsCart? {
        return inputModel?.let { ProductsCart(inputModel.cart) }
    }

}