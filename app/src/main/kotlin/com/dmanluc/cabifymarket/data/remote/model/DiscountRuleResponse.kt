package com.dmanluc.cabifymarket.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-02.
 */
class DiscountRuleResponse(@SerializedName("code") val code: String, @SerializedName("type") val type: String, @SerializedName(
    "description"
) val description: String?, @SerializedName("params") val params: RuleParamsResponse?) {

    class RuleParamsResponse(@SerializedName("quantityForFree") val freeQuantity: Int?, @SerializedName(
        "quantityToBuy"
    ) val buyQuantity: Int?, @SerializedName("bulkQuantity") val bulkQuantity: Int?, @SerializedName(
        "bulkPrice"
    ) val bulkPrice: Double?)
}