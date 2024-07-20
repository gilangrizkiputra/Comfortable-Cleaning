package com.example.comfortablecleaning_copy

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

data class TransactionStatusResponse(
    val status_code: String,
    val status_message: String,
    val transaction_id: String,
    val order_id: String,
    val gross_amount: String,
    val payment_type: String,
    val transaction_time: String,
    val transaction_status: String,
    val fraud_status: String
)

interface MidtransApiService {
    @GET("v2/{order_id}/status")
    fun getTransactionStatus(
        @Path("order_id") orderId: String,
        @Header("Authorization") authHeader: String
    ): Call<TransactionStatusResponse>
}
