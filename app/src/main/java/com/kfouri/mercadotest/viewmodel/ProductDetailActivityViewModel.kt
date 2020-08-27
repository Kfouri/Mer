package com.kfouri.mercadotest.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfouri.mercadotest.model.ProductDetailResponseModel
import com.kfouri.mercadotest.model.ProductResponseModel
import com.kfouri.mercadotest.network.ApiService
import com.kfouri.mercadotest.util.Constants
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProductDetailActivityViewModel : ViewModel() {

    private var TAG = "ProductDetailActivityViewModel"
    private var productLiveData = MutableLiveData<ProductResponseModel>()
    private var showProgress = MutableLiveData<Boolean>()

    private lateinit var product: ProductResponseModel

    private val apiService by lazy {
        ApiService.create()
    }

    fun getProduct(idProduct: String) {

        apiService.getProduct(idProduct)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showProgress(true)}
            .subscribe(object : Subscriber<ProductResponseModel>() {
                override fun onCompleted() {
                    if (Constants.ENABLE_LOG) {
                        Log.d(TAG, "getProduct() - onCompleted() - productID=$idProduct")
                    }
                    getProductDetail(idProduct)
                }

                override fun onError(e: Throwable) {
                    //showToast("Error al obtener los productos")
                    showProgress(false)
                    if (Constants.ENABLE_LOG) {
                        Log.d(TAG, "getProduct() - onError() - productID=$idProduct error="+e.message)
                    }
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
            .doOnTerminate { showProgress(false) }
            .subscribe(object : Subscriber<ProductDetailResponseModel>() {
                override fun onCompleted() {
                    if (Constants.ENABLE_LOG) {
                        Log.d(TAG, "getProductDetail() - onCompleted() - productID=$idProduct")
                    }
                    product.description = detail
                    productLiveData.value = product
                }

                override fun onError(e: Throwable) {
                    showProgress(false)
                    if (Constants.ENABLE_LOG) {
                        Log.d(TAG, "getProductDetail() - onError() - productID=$idProduct error="+e.message)
                    }
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