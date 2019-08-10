package utils

/**
 * Interface for model mapper. It provides helper method that facilitate
 * retrieving of model from outer data source layer
 *
 * @param <M> the entity model input type
 * @param <E> the entity model output type
 */
interface EntityMapper<in R, out E> {

    fun mapFrom(inputModel: R): E

}