package com.dmanluc.cabifymarket.data.remote.mapper

/**
 * Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <M> the remote model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<in R, out E> {

    fun mapFromRemote(remoteApiModel: R): E

}