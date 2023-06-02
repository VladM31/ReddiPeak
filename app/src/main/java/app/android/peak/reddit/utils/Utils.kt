package app.android.peak.reddit.utils

import java.util.*
import java.util.concurrent.TimeUnit

fun hoursSince(unixTime: Long): Long {
    val now = Date().time
    return TimeUnit.MILLISECONDS.toHours(now - unixTime)
}