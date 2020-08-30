package com.kfouri.mercadotest.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kfouri.mercadotest.R
import com.kfouri.mercadotest.util.Constants
import java.text.DecimalFormat


@BindingAdapter("condition")
fun checkCondition(textView: TextView, condition: String?) {

    condition?.let{
        textView.text = when (it) {
            Constants.CONDITION_NEW -> textView.context.getString(R.string.condition_new)
            Constants.CONDITION_USED -> textView.context.getString(R.string.condition_used)
            else -> textView.context.getString(R.string.condition_no_tag)
        }
    }

}

@BindingAdapter("price")
fun formatPrice(textView: TextView, price: Float) {
    if (price != 0.0f) {
        val formatter = DecimalFormat("$ ###,###,##0.00")
        textView.text = formatter.format(price)
        textView.visibility = View.VISIBLE
    }
}

@BindingAdapter("stock")
fun formatStock(textView: TextView, stock: Long) {
    if (stock > 0) {
        textView.text = textView.context.getString(R.string.quantity, stock.toString())
        textView.visibility = View.VISIBLE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("description")
fun checkDescription(textView: TextView, description: String?) {
    description?.let {
        textView.text = "${textView.context.getString(R.string.description)}\n\n$it"
        textView.visibility = View.VISIBLE
    }
}