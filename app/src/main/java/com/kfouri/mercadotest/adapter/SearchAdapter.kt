package com.kfouri.mercadotest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.mercadotest.GlideApp
import com.kfouri.mercadotest.R
import com.kfouri.mercadotest.model.ProductModel
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(val context: Context, private val clickListener: (ProductModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var searchList = ArrayList<ProductModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = searchList[position]
        (holder as SearchViewHolder).bind(item, clickListener, context)
    }

    fun setData(list: ArrayList<ProductModel>) {
        searchList.clear()
        searchList.addAll(list)
        notifyDataSetChanged()
    }

    class SearchViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        private val CONDITION_NEW = "new"
        private val CONDITION_USED = "used"

        fun bind(product: ProductModel, clickListener: (ProductModel) -> Unit, context: Context){
            itemView.textView_title.text = product.title
            itemView.textView_price.text = context.getString(R.string.price, product.price.toString())

            itemView.textView_free.visibility = View.GONE
            if (product.shipping.free_shipping) {
                itemView.textView_free.visibility = View.VISIBLE
                itemView.textView_free.text =  context.getString(R.string.free_shipping)
            }

            itemView.textView_condition.text = when (product.condition) {
                CONDITION_NEW -> context.getString(R.string.condition_new)
                CONDITION_USED -> context.getString(R.string.condition_used)
                else -> context.getString(R.string.condition_no_tag)
            }

            GlideApp.with(context)
                .load(product.thumbnail)
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.damaged_image)
                .fitCenter()
                .into(itemView.imageView_image)

            itemView.setOnClickListener { clickListener(product) }
        }

    }

}