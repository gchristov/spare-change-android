package com.gchristov.sparechange.common

import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class BigDecimalExtTest {

    @Test
    fun test_fromMinorUnit() {
        val fromMinorUnit = BigDecimal("1234").fromMinorUnit()
        Assert.assertEquals("12.34", fromMinorUnit.toPlainString())
    }

    @Test
    fun test_toMinorUnit() {
        val toMinorUnit = BigDecimal("12.34").toMinorUnit()
        Assert.assertEquals("1234", toMinorUnit.toPlainString())
    }

    @Test
    fun test_spareChangeRoundUp() {
        val roundedUp1 = BigDecimal("1234").fromMinorUnit().spareChangeRoundUp()
        Assert.assertEquals("0.66", roundedUp1.toPlainString())
        val roundedUp2 = BigDecimal("1200").fromMinorUnit().spareChangeRoundUp()
        Assert.assertEquals("1.00", roundedUp2.toPlainString())
        val roundedUp3 = BigDecimal("1201").fromMinorUnit().spareChangeRoundUp()
        Assert.assertEquals("0.99", roundedUp3.toPlainString())
    }
}