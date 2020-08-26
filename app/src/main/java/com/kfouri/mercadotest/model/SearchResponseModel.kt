package com.kfouri.mercadotest.model

import com.google.gson.annotations.SerializedName

data class SearchResponseModel(
    @SerializedName("site_id")  val site_id: String,
    @SerializedName("paging")  val paging: PagingModel,
    @SerializedName("results")  val results: ArrayList<ProductModel>
)

data class PagingModel(
    @SerializedName("total") val total: Long,
    @SerializedName("limit") val limit: Long
)

data class ProductModel(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("price") val price: Float,
    @SerializedName("shipping") val shipping: Shipping
)

data class Shipping(
    @SerializedName("free_shipping") val free_shipping: Boolean
)