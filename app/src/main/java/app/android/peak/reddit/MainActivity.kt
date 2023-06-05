package app.android.peak.reddit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import app.android.peak.reddit.activities.ImageViewerActivity
import app.android.peak.reddit.adapters.RedditPostAdapter
import app.android.peak.reddit.viewmodels.RedditViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: RedditViewModel = ViewModelProvider(this)[RedditViewModel::class.java]

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

        lifecycleScope.launch {
            viewModel.getPosts().collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

}