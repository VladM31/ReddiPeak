package app.android.peak.reddit.clients

import app.android.peak.reddit.models.PostContainer
import app.android.peak.reddit.models.RedditResponse
import app.android.peak.reddit.utils.RedditPaginationPosts
import app.android.peak.reddit.utils.newBuilder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class HttpRedditPostClient(
    private val gson: Gson,
    private val client: OkHttpClient,
    private val httpUrl: HttpUrl
) : RedditPostClient {

    override suspend fun find(pagination: RedditPaginationPosts): PostContainer {
        val url = httpUrl.newBuilder(pagination).build()

        val request = Request.Builder().url(url).build()


        return withContext(Dispatchers.IO) {
            val respond = client.newCall(request).execute();


            if (!respond.isSuccessful) {
                throw RuntimeException(respond.body?.string())
            }
            val response = gson.fromJson(respond.body?.string(), RedditResponse::class.java)

            return@withContext PostContainer(
                response.data.after,
                response.data.children.map { it.data }
            )
        }
    }
}