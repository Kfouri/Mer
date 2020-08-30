package com.kfouri.mercadotest.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.kfouri.mercadotest.R
import com.kfouri.mercadotest.adapter.ViewPagerAdapter
import com.kfouri.mercadotest.databinding.ActivityProductDetailBinding
import com.kfouri.mercadotest.model.ProductResponseModel
import com.kfouri.mercadotest.util.Constants.CONDITION_NEW
import com.kfouri.mercadotest.util.Constants.CONDITION_USED
import com.kfouri.mercadotest.util.Constants.ENABLE_LOG
import com.kfouri.mercadotest.util.Utils
import com.kfouri.mercadotest.viewmodel.ProductDetailActivityViewModel
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : BaseActivity() {

    private var TAG = "ProductDetailActivity"
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var dotsCount: Int = 0
    private var dots = ArrayList<ImageView>()
    private lateinit var viewModel: ProductDetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ENABLE_LOG) {
            Log.d(TAG, "onCreate()")
        }

        viewModel = ViewModelProvider(this).get(ProductDetailActivityViewModel::class.java)
        subscribe()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)

        if (!Utils.isNetworkAvailable(this)) {
            showToast(getString(R.string.no_internet_connection))
        } else {
            getProducts()
        }

        setViewPager()
    }

    private fun subscribe() {
        viewModel.onGetProduct().observe(this, Observer { showProducts(it) })
        viewModel.onShowProgress().observe(this, Observer { showProgress(it) })
        viewModel.onShowToast().observe(this, Observer { showToast(it) })
    }

    private fun getProducts() {
        intent?.let {

            val productId = it.getStringExtra(PRODUCT_ID)!!

            if (ENABLE_LOG) {
                Log.d(TAG, "getProduct() id: $productId")
            }

            viewModel.getProduct(productId)
        }
    }

    private fun setViewPager() {
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

        if (ENABLE_LOG) {
            Log.d(TAG, "showProducts() product: $product")
        }

        binding.product = product

        viewPagerAdapter.setData(product.pictures)

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
}
