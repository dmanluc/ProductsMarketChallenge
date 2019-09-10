package com.dmanluc.cabifymarket.data.local.datasource

import java.util.Calendar
import java.util.GregorianCalendar
import java.util.HashMap
import java.util.concurrent.TimeUnit

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-08-18.
 *
 * Implementation of a local data source which caches data in memory (optionally add a data validation time)
 *
 */
class CacheDataSourceImpl : CacheDataSource {

    private var expirationSeconds = TimeUnit.MINUTES.toSeconds(15).toInt()

    private val cacheMap = HashMap<Class<*>, CacheEntry<*>>()

    override fun <T : Any> save(data: T) {
        cacheMap[data.javaClass] = CacheEntry(data)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(dataClass: Class<T>): T? {
        val cache = cacheMap[dataClass] as CacheEntry<T>? ?: return null
        if (cache.isExpired) {
            cacheMap.remove(dataClass)
            return null
        }
        return cache.data
    }

    override fun clear() {
        cacheMap.clear()
    }

    override fun manageExpiration(enable: Boolean, minutesForValidation: Long) {
        expirationSeconds =
            if (enable) TimeUnit.MINUTES.toSeconds(minutesForValidation).toInt() else 0
    }

    private inner class CacheEntry<T : Any>(val data: T) {

        private val expirationDate: Calendar = GregorianCalendar.getInstance()

        val isExpired =
            if (expirationSeconds == 0) false else !GregorianCalendar.getInstance().before(
                expirationDate
            )

        init {
            expirationDate.add(Calendar.SECOND, expirationSeconds)
        }
    }

}