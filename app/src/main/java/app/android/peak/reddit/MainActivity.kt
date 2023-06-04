package app.android.peak.reddit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.RecyclerView
import app.android.peak.reddit.activities.ImageViewerActivity
import app.android.peak.reddit.adapters.RedditPostAdapter
import app.android.peak.reddit.clients.HttpRedditPostClient
import app.android.peak.reddit.clients.RedditPostClient
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



        val adapter = RedditPostAdapter(
            getString(R.string.forbiddenUrlPrefix),
            resources?.getStringArray(R.array.photoExpansionResolution)!!
        ) { url ->
            val intent = Intent(this, ImageViewerActivity::class.java).apply {
                putExtra(RedditPeakApp.IMAGE_URL, url)
            }

            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter

        val pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RedditPagingSource(makeClient()) }
        ).flow

        lifecycleScope.launch {
            pager.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
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