package app.android.peak.reddit.utils

import okhttp3.HttpUrl
import java.util.*
import java.util.concurrent.TimeUnit
import java.net.URL


fun hoursSince(unixTime: Long): Long {
    val now = System.currentTimeMillis() / 1000
    val diffInSeconds = now - unixTime
    return diffInSeconds / 3600
}

fun HttpUrl.newBuilder(pagination: RedditPaginationPosts): HttpUrl.Builder {
    val newUtlBuilder = this.newBuilder()

    return pagination.after.let { newUtlBuilder.addQueryParameter("after", it) }
        .addQueryParameter("limit", pagination.limit.toString())
}


fun isUrl(str: String?): Boolean {
    return try {
        if(str == null){
           return false
        }
        URL(str)
        true
    } catch (e: Exception) {
        false
    }
}