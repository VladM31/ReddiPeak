package app.android.peak.reddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.RecyclerView
import app.android.peak.reddit.adapters.RedditPostAdapter
import app.android.peak.reddit.clients.HttpRedditPostClient
import app.android.peak.reddit.pagingsource.RedditPagingSource
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = RedditPostAdapter { url -> }
        findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter

        val client = HttpRedditPostClient(
            Gson(),
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build(),
            getRedditPostUrl()
        )

        val pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RedditPagingSource(client) }
        ).flow

        lifecycleScope.launch {
            pager.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun getRedditPostUrl() : HttpUrl {
        return HttpUrl.Builder()
            .scheme(getString(R.string.scheme))
            .host(getString(R.string.host))
            .addPathSegment(getString(R.string.pathSegment))
            .build()
    }
}