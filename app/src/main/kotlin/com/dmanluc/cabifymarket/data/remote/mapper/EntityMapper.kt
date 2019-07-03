package com.dmanluc.cabifymarket.data.remote.mapper

/**
 * Interface for model mapper. It provides helper method that facilitate
 * retrieving of model from outer data source layer
 *
 * @param <M> the remote model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<in R, out E> {

    fun mapFromRemote(remoteApiModel: R): E

}