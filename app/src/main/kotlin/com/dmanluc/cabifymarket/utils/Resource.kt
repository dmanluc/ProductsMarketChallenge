package com.dmanluc.cabifymarket.utils

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 *
 * A generic class that holds a value with its loading status
 * @see: https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/Resource.kt
 *
 */
data class Resource<out T>(val status: Status, val data: T? = null, val error: Throwable? = null) {

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS, data, null
            )
        }

        fun <T> error(error: Throwable, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR, data, error
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING, data, null
            )
        }
    }

    enum class Status {
        SUCCESS, ERROR, LOADING
    }
}