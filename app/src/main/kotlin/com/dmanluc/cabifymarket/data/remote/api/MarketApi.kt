package com.dmanluc.cabifymarket.data.remote.api

import com.dmanluc.cabifymarket.data.remote.model.MarketApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

/**
 * Market API
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
interface MarketApi {

    @GET("bins/4bwec")
    fun getProductsAsync(): Deferred<MarketApiResponse>

}