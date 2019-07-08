package com.dmanluc.cabifymarket.domain.entity

import java.text.NumberFormat
import java.util.*

/**
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    2019-07-03.
 */
class CurrencyAmount(val amount: Double) {

    fun formatCurrency(currencyCode: CurrencyCode = CurrencyCode.EUR) : String {
        return NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(currencyCode.code)
            maximumFractionDigits = 2
            minimumFractionDigits = 0
        }.format(amount)
    }

    fun formatCurrencyInLocale(): String = currencyInLocale().format(amount)

    private fun currencyInLocale(): NumberFormat {
        return NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(Locale.getDefault())
            maximumFractionDigits = 2
            minimumFractionDigits = 0
        }
    }

    enum class CurrencyCode(val code: String) {
        EUR("EUR"),
        USD("USD")
    }

}