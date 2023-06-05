package app.android.peak.reddit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.android.peak.reddit.R
import app.android.peak.reddit.models.Post
import app.android.peak.reddit.utils.GlideHiddenRequestListener
import app.android.peak.reddit.utils.getExtensionFromUrl
import app.android.peak.reddit.utils.isUrl
import com.bumptech.glide.Glide

class RedditPostAdapter(
    private val forbiddenPrefix: String,
    private val photoExpansionResolutions : Array<String>,
    private val onClick: (String) -> Unit
) :
    PagingDataAdapter<Post, RedditPostAdapter.ViewHolder>(POST_COMPARATOR) {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.thumbnailImageView)
        private val authorView: TextView = itemView.findViewById(R.id.authorItemTextView)
        private val numCommentsView: TextView = itemView.findViewById(R.id.numCommentsTextView)
        private val timeAgoView: TextView = itemView.findViewById(R.id.timeAgoTextView)
        private val largeIcon: ImageView = itemView.findViewById(R.id.toLargeIcon)

        fun bind(post: Post) {
            authorView.text = post.author
            "Comments: ${post.numComments}".also { numCommentsView.text = it }
            "${post.hoursAgo} hours ago".also { timeAgoView.text = it }

            if (isNotValidThumbnail(post.thumbnail)) {
                imageView.visibility = View.GONE
                largeIcon.visibility = View.GONE
                return
            }
            imageView.visibility = View.VISIBLE

            Glide.with(itemView)
                .load(post.thumbnail)
                .listener(GlideHiddenRequestListener(imageView))
                .placeholder(R.drawable.photo_drawable)
                .into(imageView)

            val showUrl = if(getExtensionFromUrl(post.url!!) in photoExpansionResolutions) post.url else ""

            if(showUrl.isBlank()){
                imageView.setOnClickListener{}
                largeIcon.visibility = View.GONE
                return
            }
            largeIcon.visibility = View.VISIBLE

            largeIcon.setOnClickListener {
                onClick(showUrl)
            }

            imageView.setOnClickListener {
                onClick(showUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        post?.let { holder.bind(it) }
    }

    private fun isNotValidThumbnail(thumbnail : String?): Boolean{
        if(thumbnail == null){
            return true
        }
        return !isUrl(thumbnail) || thumbnail.startsWith(forbiddenPrefix)
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem
        }
    }
}