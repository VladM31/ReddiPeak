package app.android.peak.reddit.utils

import okhttp3.HttpUrl
import java.util.*
import java.util.concurrent.TimeUnit

fun hoursSince(unixTime: Long): Long {
    val now = Date().time
    return TimeUnit.MILLISECONDS.toHours(now - unixTime)
}

fun HttpUrl.newBuilder(pagination: RedditPaginationPosts): HttpUrl.Builder {
    val newUtlBuilder = this.newBuilder()

    return pagination.after.let { newUtlBuilder.addQueryParameter("after", it) }
        .addQueryParameter("limit", pagination.limit.toString())
}