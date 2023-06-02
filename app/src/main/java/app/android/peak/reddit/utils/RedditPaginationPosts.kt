package app.android.peak.reddit.utils

data class RedditPaginationPosts(
    val after: String?,
    val limit: Int
){
    class Builder {
        private var after: String? = null
        private var limit: Int = 25
        fun after(after: String?): Builder {
            this.after = after
            return this
        }

        fun limit(limit: Int): Builder {
            this.limit = limit
            return this
        }
        fun build(): RedditPaginationPosts {
            return RedditPaginationPosts(after, limit)
        }
    }
}
