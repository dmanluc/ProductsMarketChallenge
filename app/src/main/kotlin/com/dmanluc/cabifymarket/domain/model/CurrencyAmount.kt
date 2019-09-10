package com.dmanluc.cabifymarket.domain.model

import java.io.Serializable
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-03.
 *
 * Domain model which represents a currency amount
 *
 */
data class CurrencyAmount(val amount: Double) : Serializable {

    fun formatCurrencyInLocale(): String = currencyInLocale().format(amount)

    private fun currencyInLocale(): NumberFormat {
        return NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(Locale.getDefault())
            maximumFractionDigits = 2
            minimumFractionDigits = 0
        }
    }

}