package com.gchristov.sparechange.repository.model

import com.gchristov.sparechange.common.spareChangeRoundUp
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*

data class FeedItem(
        val feedItemUid: String,
        val categoryUid: String,
        val amount: Amount,
        val sourceAmount: Amount,
        val direction: String,
        val transactionTime: Date,
        val counterPartyName: String
) : Serializable { // TODO: Replace with Parcelable for better performance - okay for demo

    fun price(): String {
        val sign = if (isOut()) "-" else "+"
        return "$sign${amount.price()}"
    }

    fun spareChangeRoundUp(): Amount {
        return amount.spareChangeRoundUp()
    }

    fun isOut(): Boolean = "out".equals(direction, true)
}

data class Amount(
        val currency: String,
        val minorUnits: BigDecimal
) : Serializable { // TODO: Replace with Parcelable for better performance - okay for demo

    fun price(): String {
        return formatCurrency(minorUnits, currency)
    }

    fun spareChangeRoundUp(): Amount {
        return Amount(currency, minorUnits.spareChangeRoundUp())
    }

    private fun formatCurrency(value: BigDecimal, currencyCode: String): String {
        val format = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance(currencyCode)
        format.roundingMode = RoundingMode.UNNECESSARY
        format.maximumFractionDigits = 2
        return format.format(value)
    }
}
