package com.kfouri.mercadotest.network

import com.kfouri.mercadotest.model.ProductDetailResponseModel
import com.kfouri.mercadotest.model.ProductResponseModel
import com.kfouri.mercadotest.model.SearchResponseModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.http.*
import rx.Observable

interface ApiService {

    @GET("sites/{siteId}/search")
    fun searchProduct(@Path("siteId") siteId: String, @Query("q") find: String):
            Observable<SearchResponseModel>

    @GET("items/{id}")
    fun getProduct(@Path("id") id: String): Observable<ProductResponseModel>

    @GET("items/{id}/description")
    fun getProductDescription(@Path("id") id: String): Observable<ProductDetailResponseModel>

    companion object {
        fun create(): ApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.mercadolibre.com/")
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}