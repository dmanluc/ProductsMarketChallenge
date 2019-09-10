package com.dmanluc.cabifymarket.utils

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