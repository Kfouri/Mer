package com.kfouri.mercadotest.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field


object Utils {

    /**
     * Hide the softKeyboard
     * @param activity
     */
    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        view?.let {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    /**
     * Check is Network is available
     * @param context
     * @return true if is available, otherwise false
     */
    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    fun fixInputMethod(context: Context?) {
        if (context == null) {
            return
        }
        var inputMethodManager: InputMethodManager? = null
        try {
            inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        } catch (th: Throwable) {
            th.printStackTrace()
        }
        if (inputMethodManager == null) {
            return
        }
        val declaredFields: Array<Field> = inputMethodManager.javaClass.declaredFields
        for (declaredField in declaredFields) {
            try {
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true)
                }
                val obj: Any = declaredField.get(inputMethodManager)
                if (obj == null || obj !is View) {
                    continue
                }
                val view: View = obj as View
                if (view.getContext() === context) {
                    declaredField.set(inputMethodManager, null)
                } else {
                    continue
                }
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }
}