package com.kfouri.mercadotest.model

import com.google.gson.annotations.SerializedName

data class ProductDetailResponseModel(
    @SerializedName("plain_text")  val plain_text: String
    )