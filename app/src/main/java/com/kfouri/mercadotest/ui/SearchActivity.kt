package com.kfouri.mercadotest.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.mercadotest.R
import com.kfouri.mercadotest.adapter.SearchAdapter
import com.kfouri.mercadotest.model.ProductModel
import com.kfouri.mercadotest.util.Constants
import com.kfouri.mercadotest.util.Utils
import com.kfouri.mercadotest.viewmodel.SearchActivityViewModel
import kotlinx.android.synthetic.main.activity_search.*

const val PRODUCT_ID = "productId"

class SearchActivity : AppCompatActivity() {

    private val TAG = "SearchActivity"
    private val adapter = SearchAdapter(this) { product : ProductModel -> itemClicked(product) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (Constants.ENABLE_LOG) {
            Log.d(TAG, "onCreate()")
        }

        recyclerView_search.setHasFixedSize(true)
        recyclerView_search.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView_search.adapter = adapter

        val viewModel = ViewModelProviders.of(this).get(SearchActivityViewModel::class.java)
        viewModel.onProductList().observe(this, Observer { showProducts(it) })
        viewModel.onShowProgress().observe(this, Observer { showProgress(it) })

        button_search.setOnClickListener {

            if (Constants.ENABLE_LOG) {
                Log.d(TAG, "SearchButton pressed - Search Field empty?:"+editText_search.text.isEmpty())
            }

            if (editText_search.text.isNotEmpty()) {
                hideKeyboard()
                viewModel.searchProduct(editText_search.text.toString())
            } else {
                Toast.makeText(this, getString(R.string.search_field_empty), Toast.LENGTH_LONG).show()
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                val cnt = adapter.itemCount
                textView_emptyList.text = getString(R.string.empty_list)
                textView_emptyList.visibility = if (cnt > 0) View.GONE else View.VISIBLE
            }
        })
    }

    private fun showProgress(value: Boolean) {
        linearLayout_progress.visibility = if (value) View.VISIBLE else View.GONE
        progressBar.visibility = if (value) View.VISIBLE else View.GONE
    }

    private fun hideKeyboard() {
        Utils.hideKeyboard(this)
    }

    private fun showProducts(list: ArrayList<ProductModel>) {
        if (Constants.ENABLE_LOG) {
            Log.d(TAG, "showProducts() - list size: "+list.size)
        }
        adapter.setData(list)
    }

    private fun itemClicked(product: ProductModel) {

        if (Constants.ENABLE_LOG) {
            Log.d(TAG, "itemClicked - productId: "+product.id)
        }

        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra(PRODUCT_ID, product.id)
        startActivity(intent)
    }
}
