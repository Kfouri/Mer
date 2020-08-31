package com.kfouri.mercadotest.ui

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_product_detail.*

open class BaseActivity : AppCompatActivity() {

    /**
     * Show progress dialog
     * @param value on/off dialog
     */
    protected fun showProgress(value: Boolean) {
        linearLayout_progress.visibility = if (value) View.VISIBLE else View.GONE
        progressBar.visibility = if (value) View.VISIBLE else View.GONE
    }

    /**
     * Show toast
     * @param error Error message
     */
    protected fun showToast(error: String) {
        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
    }
}