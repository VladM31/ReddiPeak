package app.android.peak.reddit.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.android.peak.reddit.clients.RedditPostClient
import app.android.peak.reddit.models.Post
import app.android.peak.reddit.utils.RedditPaginationPosts

class RedditPagingSource(
    private val redditPostClient: RedditPostClient
) : PagingSource<String, Post>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        return try {
            val currentLoadingPageKey = params.key

            val response = redditPostClient.find(
                RedditPaginationPosts.Builder()
                    .limit(params.loadSize)
                    .after(currentLoadingPageKey)
                    .build()
            )

            val prevKey = if ( response.content.isEmpty()) null else currentLoadingPageKey
            val nextKey = if ( response.content.isEmpty()) null else response.after

            LoadResult.Page(
                data = response.content,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}