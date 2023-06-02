package app.android.peak.reddit.models

data class RedditResponse(
    val data: RedditData
)

data class RedditData(
    val children: List<RedditChildren>,
    val after : String?
)

data class RedditChildren(
    val data: Post
)
