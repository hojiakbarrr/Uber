package com.example.uber.api

import com.example.uber.model.CreatUserResponse
import com.example.uber.model.SignInrResponse
import com.example.uber.model.User
import com.example.uber.orderModel.GetOrderResponse
import com.example.uber.orderModel.Order
import com.example.uber.orderModel.OrderResponse
import com.example.uber.orderModel.ServerResponse
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("users")
    fun creatNewUser(@Body user: User): Call<CreatUserResponse>

    @GET("login")
    fun signIn(
        @Query("username") username: String,
        @Query("password") password: String,

        ): Call<SignInrResponse>

    @POST("classes/Order")
    fun createOrder(@Body order: Order): Call<OrderResponse>


    @GET("classes/Order")
    fun getOrders(): Call<GetOrderResponse>


    @PUT("classes/Order/{objectId}")
    fun updateOrder(
        @Path("objectId") objectId: String,
        @Body order: Order
    ): Call<ServerResponse>

}