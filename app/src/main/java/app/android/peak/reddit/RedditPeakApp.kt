package app.android.peak.reddit

import android.app.Application
import app.android.peak.reddit.clients.HttpRedditPostClient
import app.android.peak.reddit.clients.RedditPostClient
import com.google.gson.Gson
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class RedditPeakApp : Application() {
    private lateinit var redditPostClient : RedditPostClient

    fun redditPostClient(): RedditPostClient = redditPostClient

    override fun onCreate() {
        super.onCreate()
        redditPostClient = makeClient()
    }

    companion object {
        const val IMAGE_URL = "IMAGE_URL"
    }

    private fun makeClient(): RedditPostClient {
        return HttpRedditPostClient(
            Gson(),
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build(),
            getRedditPostUrl()
        )
    }

    private fun getRedditPostUrl(): HttpUrl {
        return HttpUrl.Builder()
            .scheme(getString(R.string.scheme))
            .host(getString(R.string.host))
            .addPathSegment(getString(R.string.pathSegment))
            .build()
    }
}