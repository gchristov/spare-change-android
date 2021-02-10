package com.gchristov.sparechange.common

import java.math.BigDecimal

private const val UNIT_SCALE = 2
private val UNIT_MINOR = BigDecimal(100) // TODO: Perhaps this shouldn't be hardcoded but rather determined based on the currency

/**
 * Converts minor unit into BigDecimal. For example, 1234 becomes 12.34.
 */
fun BigDecimal.fromMinorUnit(): BigDecimal {
    return divide(UNIT_MINOR).setScale(UNIT_SCALE) // e.g. 1234 -> 12.34
}

/**
 * Converts BigDecimal into minor unit. For example, 12.34 becomes 1234.
 */
fun BigDecimal.toMinorUnit(): BigDecimal {
    return multiply(UNIT_MINOR).setScale(0) // e.g. 12.34 -> 1234
}

/**
 * Rounds up spare change to the nearest major unit. For example, 12.34 would output 0.66 spare change units (in GBP that would be £0.66).
 *
 * Note that 12.00 will round up to 1.00, which seems to be the behaviour in other spare change implementations in banking apps.
 */
fun BigDecimal.spareChangeRoundUp(): BigDecimal {
    val fractionPart = this.remainder(BigDecimal.ONE) // e.g. 12.34 -> 0.34
    return BigDecimal.ONE.subtract(fractionPart) // e.g. 1.00 - 0.34 = 0.66
}