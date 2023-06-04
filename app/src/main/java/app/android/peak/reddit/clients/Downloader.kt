package app.android.peak.reddit.clients

interface Downloader {
    suspend fun downloadFile(url : String)
}