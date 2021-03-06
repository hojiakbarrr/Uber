package com.example.uber.orderModel

import com.google.gson.annotations.SerializedName

data class GetOrderResponse(
    @SerializedName("results")
    var orderList: List<Order>? = null
)