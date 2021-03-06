package com.dmanluc.cabifymarket.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 *
 *  Model for Market API response
 *
 */
class MarketApiResponse(@SerializedName("products") val products: List<ProductApiResponse>?) {

    class ProductApiResponse(
        @SerializedName("code") val id: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("price") val price: Double
    )

}