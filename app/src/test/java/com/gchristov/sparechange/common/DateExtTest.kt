package com.gchristov.sparechange.common

import org.junit.Assert
import org.junit.Test
import java.util.*

class DateExtTest {

    @Test
    fun test_weeksFromNow() {
        val now = Calendar.getInstance()
        val weeksFromNow = getCalendarForDate(now.time.weeksFromNow(-1))
        Assert.assertEquals(now[Calendar.WEEK_OF_YEAR] - 1, weeksFromNow[Calendar.WEEK_OF_YEAR])
        Assert.assertEquals(0, weeksFromNow[Calendar.HOUR])
        Assert.assertEquals(0, weeksFromNow[Calendar.MINUTE])
        Assert.assertEquals(0, weeksFromNow[Calendar.SECOND])
        Assert.assertEquals(0, weeksFromNow[Calendar.MILLISECOND])
    }

    private fun getCalendarForDate(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }
}