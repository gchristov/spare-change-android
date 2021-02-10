package com.gchristov.sparechange.repository.model

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal
import java.util.*

class FeedItemTest {

    private val amount = mock<Amount>()

    @Test
    fun test_priceSummary() {
        whenever(amount.price()).thenReturn("1234")
        // Out
        var feedItem = getFeedItem("OUT")
        Assert.assertEquals("-1234", feedItem.price())
        // In
        feedItem = getFeedItem("IN")
        Assert.assertEquals("+1234", feedItem.price())
    }

    @Test
    fun test_out() {
        // Out
        var feedItem = getFeedItem("OUT")
        Assert.assertTrue(feedItem.isOut())
        // In
        feedItem = getFeedItem("IN")
        Assert.assertFalse(feedItem.isOut())
    }

    private fun getFeedItem(direction: String): FeedItem {
        return FeedItem(
                "id",
                "category",
                amount,
                amount,
                direction,
                Date(),
                "My Store"
        )
    }
}

class AmountTest {

    @Test
    fun test_priceSummary() {
        val amount = getAmount()
        Assert.assertEquals("£12.34", amount.price())
    }

    @Test
    fun test_spareChangeRoundUp() {
        val amount = getAmount()
        Assert.assertEquals(Amount("GBP", BigDecimal("0.66")), amount.spareChangeRoundUp())
    }

    private fun getAmount(): Amount {
        return Amount(
                "GBP",
                BigDecimal("12.34")
        )
    }
}