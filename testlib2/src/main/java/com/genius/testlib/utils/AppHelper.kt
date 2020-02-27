package com.genius.testlib.utils

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.genius.testlib.BuildConfig
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


/**
 * Created by geniuS on 11/27/2019.
 */
object AppHelper {

    fun changeStatusBarColor(color: Int, window: Window) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    fun debugToast(context: Context, msg: String?) {
        if (BuildConfig.DEBUG) Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    @JvmStatic
    fun showToast(context: Context, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(context: Context, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    @JvmStatic
    fun printLog(msg: String?) {
        if (BuildConfig.DEBUG) {
            val chunkSize = 2048
            if (msg!!.length >= chunkSize) {
                var i = 0
                while (i < msg.length) {
                    Log.e(
                        "Debug ",
                        msg.substring(i, msg.length.coerceAtMost(i + chunkSize))
                    )
                    i += chunkSize
                }
            } else {
                Log.e("Debug ", msg)
            }
        }
    }

    fun printStackTrace(exception: Exception) {
        if (BuildConfig.DEBUG)
            printLog(exception.toString())
        printLog(exception.message.toString())
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun pixelToDp(context: Context, px: Int): Int {
        return (px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

    fun getVersionCodeOfApp(context: Context): String? {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    fun hideKeyboard(context: Context, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val imm = context.getSystemService(
                AppCompatActivity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    @JvmStatic
    fun hideKeyboard(context: Context) {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                inputManager.hideSoftInputFromWindow(
                    Objects.requireNonNull((context as AppCompatActivity).currentFocus)?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    fun getCurrentDate(): String? {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    }

    fun showImageChooseDialog(context: Context?, imageChooserCallback: ImageChooserCallback) {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context!!)
        builder.setItems(items) { dialog: DialogInterface, item: Int ->
            when {
                items[item] == "Take Photo" -> {
                    imageChooserCallback.onChosen(0)
                }
                items[item] == "Choose from Gallery" -> {
                    imageChooserCallback.onChosen(1)
                }
                items[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    interface ImageChooserCallback {
        fun onChosen(chosenItem: Int)
    }

    fun setImageDrawable(
        imageView: ImageView,
        drawableId: Int
    ) {
        if (Build.VERSION.SDK_INT >= 21) {
            imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, drawableId))
        } else if (Build.VERSION.SDK_INT >= 16) {
            imageView.setImageDrawable(imageView.context.resources.getDrawable(drawableId))
        }
    }

    //inline function to convert list to arraylist
    fun <T> List<T>.toArrayList(): ArrayList<T> {
        return ArrayList(this)
    }
}