package com.dmanluc.cabifymarket.utils

/**
 * Interface for model mapper. It provides helper method that facilitate
 * retrieving of model from outer data source layer
 *
 * @param <M> the entity model input type
 * @param <E> the entity model output type
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 */
interface EntityMapper<in R, out E> {

    fun mapFrom(inputModel: R): E

}