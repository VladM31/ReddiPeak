package app.android.peak.reddit.utils

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

fun useFullScreen(activity: AppCompatActivity){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.window.setDecorFitsSystemWindows(false)
        activity.window.insetsController?.let {
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun showMessageOKCancel(message: String, context : Context, okListener: DialogInterface.OnClickListener) {
    AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show()
}