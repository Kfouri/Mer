package com.kfouri.mercadotest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kfouri.mercadotest.GlideApp
import com.kfouri.mercadotest.R
import com.kfouri.mercadotest.model.PicturesModel

class ViewPagerAdapter(var context: Context) : PagerAdapter() {

    private var layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var imageList: ArrayList<PicturesModel>

    init {
        imageList = ArrayList()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.viewpager_layout, null)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView_viewPager)

        GlideApp.with(context)
            .load(imageList[position].url)
            .placeholder(R.drawable.loading_image)
            .error(R.drawable.damaged_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(imageView)

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    fun setData(list: ArrayList<PicturesModel>) {
        imageList = list
        notifyDataSetChanged()
    }
}