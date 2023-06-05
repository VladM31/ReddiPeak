package app.android.peak.reddit.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.android.peak.reddit.RedditPeakApp
import app.android.peak.reddit.models.Post
import app.android.peak.reddit.pagingsource.RedditPagingSource
import kotlinx.coroutines.flow.Flow

class RedditViewModel(app : Application) : AndroidViewModel(app) {
    private val redditPeakApp: RedditPeakApp = app as RedditPeakApp
    private var currentSearchResult: Flow<PagingData<Post>>? = null

    fun getPosts(): Flow<PagingData<Post>> {
        if (currentSearchResult != null) {
            return currentSearchResult!!
        }

        currentSearchResult = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { RedditPagingSource(redditPeakApp.redditPostClient()) }
        ).flow.cachedIn(viewModelScope)

        return currentSearchResult!!
    }
}