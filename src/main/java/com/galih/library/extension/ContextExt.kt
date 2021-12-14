package com.galih.library.extension

import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

fun Context.getColorCompat(color: Int) = ContextCompat.getColor(this, color)

fun Context.getLayoutInflater() =
    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

fun Context.dpToPx(dp: Int): Int {
    val resource = this.resources
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        resource.displayMetrics
    ).roundToInt()
}

/**
 * Method used to easily retrieve display size from [Context].
 */
fun Context.getDisplaySize() = Point().apply {
    getWindowManager().defaultDisplay.getSize(this)
}

/**
 * Method used to easily retrieve [WindowManager] from [Context].
 */
fun Context.getWindowManager() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

@Suppress("ReturnCount")
fun Context.isNetworkConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            return hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        }
    } else {
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
    return false
}
