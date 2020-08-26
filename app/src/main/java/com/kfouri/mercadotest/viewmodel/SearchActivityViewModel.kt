package com.kfouri.mercadotest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.mercadotest.model.ProductModel
import com.kfouri.mercadotest.model.SearchResponseModel
import com.kfouri.mercadotest.network.ApiService
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class SearchActivityViewModel : ViewModel() {

    private var list = ArrayList<ProductModel>()

    var productList = MutableLiveData<ArrayList<ProductModel>>()
    var showProgress = MutableLiveData<Boolean>()

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
                    productList.value = list
                }

                override fun onError(e: Throwable) {
                    //showToast("Error al obtener los productos")
                    showProgress(false)
                    Log.d("Kafu", "Error al obtener los productos (search)"+e.message)
                }

                override fun onNext(result: SearchResponseModel) {
                    list = result.results
                }

            })
    }

    private fun showProgress(value: Boolean) {
        showProgress.value = value
    }

    fun onProductList() = productList
    fun onShowProgress() = showProgress
}