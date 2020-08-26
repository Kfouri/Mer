package com.kfouri.mercadotest.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kfouri.mercadotest.GlideApp
import com.kfouri.mercadotest.R
import com.kfouri.mercadotest.model.ProductModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(private val clickListener: (ProductModel) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var searchList = ArrayList<ProductModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = searchList[position]
        (holder as SearchViewHolder).bind(item, clickListener)
    }

    fun setData(list: ArrayList<ProductModel>) {
        searchList.clear()
        searchList.addAll(list)
        notifyDataSetChanged()
    }

    class SearchViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(product: ProductModel,  clickListener: (ProductModel) -> Unit){
            itemView.textView_title.text = product.title
            itemView.imageView_image.loadSVG(product.thumbnail)
            itemView.textView_price.text = product.price.toString()
            itemView.textView_free.text = product.shipping.free_shipping.toString()
            itemView.setOnClickListener { clickListener(product) }
        }

        private fun ImageView.loadSVG(url: String) {
            GlideApp.with(this)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_search_black_24dp)
                .fitCenter()
                .into(this)
        }
    }

}