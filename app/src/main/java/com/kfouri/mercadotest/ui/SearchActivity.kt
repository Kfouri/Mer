package com.kfouri.mercadotest.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import java.lang.reflect.Field


const val PRODUCT_ID = "productId"

class SearchActivity : BaseActivity() {

    private val TAG = "SearchActivity"
    private val adapter = SearchAdapter(this) { product : ProductModel -> itemClicked(product) }
    private lateinit var viewModel: SearchActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        if (Constants.ENABLE_LOG) {
            Log.d(TAG, "onCreate()")
        }

        setRecyclerView()

        viewModel = ViewModelProviders.of(this).get(SearchActivityViewModel::class.java)
        subscribe()

        button_search.setOnClickListener {
            if (Constants.ENABLE_LOG) {
                Log.d(TAG, "SearchButton pressed - Search Field empty?:"+editText_search.text.isEmpty())
            }

            if (!Utils.isNetworkAvailable(this)) {
                showToast(getString(R.string.no_internet_connection))
            } else {
                if (editText_search.text.isNotEmpty()) {
                    hideKeyboard()
                    viewModel.searchProduct(editText_search.text.toString())
                } else {
                    showToast(getString(R.string.search_field_empty))
                }
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

    private fun setRecyclerView() {
        recyclerView_search.setHasFixedSize(true)
        recyclerView_search.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerView_search.adapter = adapter
    }

    private fun subscribe() {
        viewModel.onProductList().observe(this, Observer { showProducts(it) })
        viewModel.onShowProgress().observe(this, Observer { showProgress(it) })
        viewModel.onShowToast().observe(this, Observer { showToast(it) })
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

    override fun onDestroy() {
        super.onDestroy()
        val im: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val fields =
            arrayOf("mCurRootView", "mServedView", "mNextServedView")
        try {
            for (filedStr in fields) {
                val field: Field = InputMethodManager::class.java.getDeclaredField(filedStr)
                field.isAccessible = true
                val mCurRootView: Any = field.get(im)!!
                if (mCurRootView is View) {
                    val context: Context = mCurRootView.context
                    if (context === this) {
                        field.set(im, null)
                    }
                }
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
    }
}
