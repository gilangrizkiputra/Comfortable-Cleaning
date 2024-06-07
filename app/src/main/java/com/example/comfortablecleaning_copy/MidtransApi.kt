package com.example.comfortablecleaning_copy

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Model data untuk permintaan transaksi
data class TransactionRequest(
    val transaction_details: TransactionDetails,
    val item_details: List<ItemDetail>,
    val customer_details: CustomerDetail
)

data class TransactionDetails(
    val order_id: String,
    val gross_amount: Double
)

data class ItemDetail(
    val id: String,
    val price: Double,
    val quantity: Int,
    val name: String
)

data class CustomerDetail(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String
)

// API interface untuk Midtrans
interface MidtransApi {
    @Headers("Content-Type: application/json")
    @POST("charge/") // Endpoint sesuai dengan server PHP
    fun createSnapToken(@Body request: TransactionRequest): Call<ResponseBody>
}

