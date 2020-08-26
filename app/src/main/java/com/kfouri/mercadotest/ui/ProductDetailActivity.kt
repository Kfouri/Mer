package com.kfouri.mercadotest.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.kfouri.mercadotest.R
import com.kfouri.mercadotest.adapter.ViewPagerAdapter
import com.kfouri.mercadotest.model.ProductResponseModel
import com.kfouri.mercadotest.viewmodel.ProductDetailActivityViewModel
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var dotsCount: Int = 0
    private var dots = ArrayList<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val viewModel = ViewModelProviders.of(this).get(ProductDetailActivityViewModel::class.java)
        viewModel.onGetProduct().observe(this, Observer { showProducts(it) })
        viewModel.onShowProgress().observe(this, Observer { showProgress(it) })

        intent?.let {
                viewModel.getProduct(it.getStringExtra("idProduct")!!)
        }

        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager_images.adapter = viewPagerAdapter
        viewPager_images.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots[i].setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.nonactive_dot
                        )
                    )
                }
                dots[position].setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.active_dot
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun showProducts(product: ProductResponseModel) {
        Log.d("Kafu", "List: "+product.title+" --- "+product.description)
        textView_title.text = product.title
        viewPagerAdapter.setData(product.pictures)
        textView_price.text = product.price.toString()
        textView_description.text = product.description?.plain_text
        setDots()
    }

    private fun setDots() {
        dotsCount = viewPagerAdapter.count

        for (i in 0 until dotsCount) {
            val dot = ImageView(this)
            dot.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nonactive_dot))

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(8, 0, 8, 0);

            dots.add(dot)
            sliderDots.addView(dot, params)
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))
    }

    private fun showProgress(value: Boolean) {
        linearLayout_progress.visibility = if (value) View.VISIBLE else View.GONE
        progressBar.visibility = if (value) View.VISIBLE else View.GONE
    }
}
