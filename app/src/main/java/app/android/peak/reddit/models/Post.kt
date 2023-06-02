package app.android.peak.reddit.models

import app.android.peak.reddit.utils.hoursSince
import com.google.gson.annotations.SerializedName

data class Post(
    val id: String,
    val author: String,
    @SerializedName("created_utc")
    val createdUtc: Long,
    val thumbnail: String?,
    @SerializedName("num_comments")
    val numComments: Int
) {
    val hoursAgo: Long
        get() = hoursSince(createdUtc)
}

