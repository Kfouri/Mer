package com.kfouri.mercadotest.ui

import android.annotation.SuppressLint
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
import com.kfouri.mercadotest.util.Utils
import com.kfouri.mercadotest.viewmodel.SearchActivityViewModel
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private val adapter = SearchAdapter { product : ProductModel -> itemClicked(product) }
    private val list = ArrayList<ProductModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val recyclerView = recyclerView_search
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView.adapter = adapter

        adapter.setData(list)

        val viewModel = ViewModelProviders.of(this).get(SearchActivityViewModel::class.java)
        viewModel.onProductList().observe(this, Observer { showProducts(it) })
        viewModel.onShowProgress().observe(this, Observer { showProgress(it) })

        button_search.setOnClickListener {
            if (editText_search.text.isNotEmpty()) {
                hideKeyboard()
                viewModel.searchProduct(editText_search.text.toString())
            } else {
                Toast.makeText(this, "Campo de búsqueda vacío", Toast.LENGTH_LONG).show()
            }
        }

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                val cnt = adapter.itemCount
                textView_emptyList.text = "Lista vacía"//getString(R.string.es_empty_list)
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
        adapter.setData(list)
    }

    private fun itemClicked(product: ProductModel) {
        Log.d("Kafu", "idProduct: "+product.id)
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("idProduct", product.id)
        startActivity(intent)
    }
}
