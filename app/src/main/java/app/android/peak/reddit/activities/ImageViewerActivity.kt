package app.android.peak.reddit.activities

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import app.android.peak.reddit.R
import app.android.peak.reddit.RedditPeakApp
import app.android.peak.reddit.clients.Downloader
import app.android.peak.reddit.clients.ImageDownloader
import app.android.peak.reddit.utils.showMessageOKCancel
import app.android.peak.reddit.utils.useFullScreen
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ImageViewerActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        supportActionBar?.hide()
        useFullScreen(this)

        val url = intent.getStringExtra(RedditPeakApp.IMAGE_URL)
        val downloader: Downloader = ImageDownloader(this)

        val imageView = findViewById<ImageView>(R.id.imageViewer)
        val fabDownload = findViewById<FloatingActionButton>(R.id.fabDownload)

        imageView.setOnClickListener {
            if (fabDownload.isShown) {
                fabDownload.hide()
            } else {
                fabDownload.show()
            }
        }

        fabDownload.setOnClickListener {
            if (checkPermission()) {
                lifecycleScope.launch {
                    downloader.downloadFile(url!!)
                }
            } else {
                requestPermission()
            }
        }


        Glide.with(this)
            .load(url)
            .into(imageView)
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSION_REQUEST_CODE) {
            return
        }

        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            !shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
            return
        }

        showMessageOKCancel("You need to allow access permissions", this@ImageViewerActivity)
        { _, _ ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
            }
        }
    }
}


