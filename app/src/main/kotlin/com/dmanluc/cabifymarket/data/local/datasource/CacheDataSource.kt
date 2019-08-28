package com.dmanluc.cabifymarket.data.local.datasource

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-18.
 */
interface CacheDataSource {

    fun <T : Any> save(data: T)

    fun <T : Any> get(dataClass: Class<T>): T?

    fun clear()

    fun manageExpiration(enable: Boolean, minutesForValidation: Long = 10)

}