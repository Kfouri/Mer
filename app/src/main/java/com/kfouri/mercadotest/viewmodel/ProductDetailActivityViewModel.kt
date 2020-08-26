package com.kfouri.mercadotest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.mercadotest.model.ProductDetailResponseModel
import com.kfouri.mercadotest.model.ProductResponseModel
import com.kfouri.mercadotest.model.SearchResponseModel
import com.kfouri.mercadotest.network.ApiService
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProductDetailActivityViewModel : ViewModel() {

    private var productLiveData = MutableLiveData<ProductResponseModel>()
    var showProgress = MutableLiveData<Boolean>()

    private lateinit var product: ProductResponseModel

    private val apiService by lazy {
        ApiService.create()
    }

    fun getProduct(idProduct: String) {

        apiService.getProduct(idProduct)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            ?.doOnSubscribe { showLoading() }
//            ?.doOnTerminate { hideLoading() }
            .subscribe(object : Subscriber<ProductResponseModel>() {
                override fun onCompleted() {
                    getProductDetail(idProduct)
                }

                override fun onError(e: Throwable) {
                    //showToast("Error al obtener los productos")
                    //hideLoading()
                    Log.d("Kafu", "Error al obtener los productos "+e.message)
                }

                override fun onNext(result: ProductResponseModel) {
                    product = result
                }

            })
    }

    private fun getProductDetail(idProduct: String) {

        var detail = ProductDetailResponseModel("")

        apiService.getProductDescription(idProduct)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress(true)}
            .doOnTerminate { showProgress(false) }
            .subscribe(object : Subscriber<ProductDetailResponseModel>() {
                override fun onCompleted() {
                    Log.d("Kafu", "Detalle onCompleted()")
                    product.description = detail
                    productLiveData.value = product
                }

                override fun onError(e: Throwable) {
                    //showToast("Error al obtener los productos")
                    //hideLoading()
                    Log.d("Kafu", "Error al obtener los Detalle del productos "+e.message)
                    productLiveData.value = product
                }

                override fun onNext(result: ProductDetailResponseModel) {
                    detail = result
                }

            })

    }

    private fun showProgress(value: Boolean) {
        showProgress.value = value
    }

    fun onGetProduct() = productLiveData
    fun onShowProgress() = showProgress
}