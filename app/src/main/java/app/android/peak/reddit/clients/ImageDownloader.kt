package app.android.peak.reddit.clients

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import app.android.peak.reddit.utils.getExtensionFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImageDownloader(context: Context): Downloader {
    private val downloadManager : DownloadManager = context.getSystemService(DownloadManager::class.java)

    override suspend fun downloadFile(url: String){
        val extension = getExtensionFromUrl(url)

        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/${extension}")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("file_${System.currentTimeMillis()}.${extension}")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"file.${extension}")
        return withContext(Dispatchers.IO) {
            downloadManager.enqueue(request)
        }
    }
}