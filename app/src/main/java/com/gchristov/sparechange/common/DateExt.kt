package com.gchristov.sparechange.common

import org.apache.commons.lang3.time.DateUtils
import java.util.*

fun Date.weeksFromNow(weeks: Int): Date {
    // Clear date properties to start at 00:00am
    var date = DateUtils.setHours(this, 0)
    date = DateUtils.setMinutes(date, 0)
    date = DateUtils.setSeconds(date, 0)
    date = DateUtils.setMilliseconds(date, 0)
    // Offset weeks
    return DateUtils.addWeeks(date, weeks)
}