package com.example.comfortablecleaning_copy

import android.content.Context
import android.content.Intent
import android.util.Base64
import android.widget.Toast
import com.example.comfortablecleaning_copy.Customer.FormPembayaran.FormPaymentActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object PaymentHMidtrans {

    fun generatePaymentLink(context: Context, totalAmount: Int, customerDetails: JSONObject, itemDetails: JSONArray, orderId: String) {
        val client = OkHttpClient()
        //API
        val url = "LINK_URL_MIDTRANS"

        val json = JSONObject().apply {
            put("transaction_details", JSONObject().apply {
                put("order_id", orderId)
                put("gross_amount", totalAmount)
            })
            put("item_details", itemDetails)
            put("customer_details", customerDetails)
        }

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Basic ${Base64.encodeToString("YOUR_SERVER_KEY".toByteArray(), Base64.NO_WRAP)}")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                (context as? FormPaymentActivity)?.runOnUiThread {
                    Toast.makeText(context, "Failed to create payment URL. ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)
                    val paymentUrl = jsonResponse.getString("redirect_url")
                    (context as? FormPaymentActivity)?.runOnUiThread {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = android.net.Uri.parse(paymentUrl)
                        context.startActivity(intent)
                    }
                } else {
                    (context as? FormPaymentActivity)?.runOnUiThread {
                        Toast.makeText(context, "Failed to create payment URL. ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun checkPaymentStatus(context: Context, orderId: String, callback: (String) -> Unit) {
        val client = OkHttpClient()
        val url = "LINK_URL_MIDTRANS"

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Basic ${Base64.encodeToString("YOUR_SERVER_KEY".toByteArray(), Base64.NO_WRAP)}")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                (context as? FormPaymentActivity)?.runOnUiThread {
                    Toast.makeText(context, "Failed to check payment status. ${e.message}", Toast.LENGTH_LONG).show()
                }
                callback("failure")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)
                    if (jsonResponse.has("transaction_status")) {
                        val transactionStatus = jsonResponse.getString("transaction_status")
                        (context as? FormPaymentActivity)?.runOnUiThread {
                            callback(transactionStatus)
                        }
                    } else {
                        (context as? FormPaymentActivity)?.runOnUiThread {
                            //Toast.makeText(context, "Silahkan Lengkapi Form", Toast.LENGTH_LONG).show()
                            callback("failure")
                        }
                    }
                } else {
                    (context as? FormPaymentActivity)?.runOnUiThread {
                        Toast.makeText(context, "Failed to check payment status. ${response.message}", Toast.LENGTH_LONG).show()
                        callback("failure")
                    }
                }
            }
        })
    }
}

