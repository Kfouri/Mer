package com.kfouri.mercadotest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.mercadotest.model.ProductModel
import com.kfouri.mercadotest.model.SearchResponseModel
import com.kfouri.mercadotest.network.ApiService
import com.kfouri.mercadotest.util.Constants
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class SearchActivityViewModel : ViewModel() {

    private var TAG = "SearchActivityViewModel"
    private var list = ArrayList<ProductModel>()

    private var productList = MutableLiveData<ArrayList<ProductModel>>()
    private var showProgress = MutableLiveData<Boolean>()
    private var showToast = MutableLiveData<String>()

    private val apiService by lazy {
        ApiService.create()
    }

    fun searchProduct(find: String) {

        apiService.searchProduct("MLA", find)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress(true)}
            .doOnTerminate { showProgress(false) }
            .subscribe(object : Subscriber<SearchResponseModel>() {
                override fun onCompleted() {
                    if (Constants.ENABLE_LOG) {
                        Log.d(TAG, "searchProduct() - onCompleted() - find=$find")
                    }
                    productList.value = list
                }

                override fun onError(e: Throwable) {
                    showToast(e.message.toString())
                    showProgress(false)
                    if (Constants.ENABLE_LOG) {
                        Log.d(TAG, "searchProduct() - onError() - find=$find error="+e.message)
                    }
                }

                override fun onNext(result: SearchResponseModel) {
                    list = result.results
                }

            })
    }

    private fun showToast(error: String) {
        showToast.value = error
    }

    private fun showProgress(value: Boolean) {
        showProgress.value = value
    }

    fun onProductList() = productList
    fun onShowProgress() = showProgress
    fun onShowToast() = showToast
}