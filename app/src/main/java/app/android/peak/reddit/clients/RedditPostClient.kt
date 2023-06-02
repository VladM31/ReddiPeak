package app.android.peak.reddit.clients

import app.android.peak.reddit.models.PostContainer
import app.android.peak.reddit.utils.RedditPaginationPosts


interface RedditPostClient {
    suspend fun find(pagination: RedditPaginationPosts) : PostContainer
}