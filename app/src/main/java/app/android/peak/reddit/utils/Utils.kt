package app.android.peak.reddit.utils

import okhttp3.HttpUrl
import java.io.File
import java.net.URI
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

fun getExtensionFromUrl(url: String): String? {
    return try {
        val file = File(URI(url).path)
        file.extension.ifEmpty { null }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
