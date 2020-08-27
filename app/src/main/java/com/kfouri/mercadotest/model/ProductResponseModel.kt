package com.kfouri.mercadotest.model

import androidx.versionedparcelable.NonParcelField
import com.google.gson.annotations.SerializedName

data class ProductResponseModel(
    @SerializedName("id")  val id: String,
    @SerializedName("title")  val title: String,
    @SerializedName("price")  val price: Float,
    @SerializedName("thumbnail")  val thumbnail: String,
    @SerializedName("pictures")  val pictures: ArrayList<PicturesModel>,
    @SerializedName("condition")  val condition: String,
    @SerializedName("available_quantity")  val availableQuantity: Long,
    @SerializedName("warranty")  val warranty: String,
    @NonParcelField
    var description: ProductDetailResponseModel?
)

data class PicturesModel(
    @SerializedName("id")  val id: String,
    @SerializedName("url")  val url: String
)