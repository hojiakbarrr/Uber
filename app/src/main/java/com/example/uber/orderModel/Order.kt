package com.example.uber.orderModel

data class Order(
    var username: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var isTaken: Boolean = false,
    var driver: String? = null,
    var location: GeoPoint? = null,
    var objectId: String? = null
)