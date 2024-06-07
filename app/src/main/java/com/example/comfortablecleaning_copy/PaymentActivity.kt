package com.example.comfortablecleaning_copy

import android.os.Bundle
import android.util.Base64
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.comfortablecleaning_copy.databinding.ActivityPaymentBinding
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

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnKitabTs.setOnClickListener { generatePaymentLink() }
    }

    private fun generatePaymentLink() {
        val client = OkHttpClient()
        val url = "https://cleancomfortable.my.id/Midtrans.php/charge/"

        val json = JSONObject().apply {
            put("transaction_details", JSONObject().apply {
                put("order_id", "Hammad-StoreID-" + System.currentTimeMillis())
                put("gross_amount", 190000) // Adjust amount as per your calculation
            })
            put("item_details", JSONArray().apply {
                put(JSONObject().apply {
                    put("id", "BUKU_101")
                    put("price", 95000)
                    put("quantity", 2)
                    put("name", "Tadzkirotus Sami")
                })
            })
            put("customer_details", JSONObject().apply {
                put("first_name", "Asep")
                put("last_name", "Sutisna")
                put("email", "yummi2102@gmail.com")
                put("phone", "081775205889")
                put("billing_address", JSONObject().apply {
                    put("address", "Cikeas, Nagrak")
                    put("city", "Bogor")
                    put("postal_code", "16967")
                })
                put("shipping_address", JSONObject().apply {
                    put("address", "Cikeas, Nagrak")
                    put("city", "Bogor")
                    put("postal_code", "16967")
                })
            })
        }

        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Basic ${Base64.encodeToString("SB-Mid-server-Xzba3_5u-lTBv-e71pfEXQSw".toByteArray(), Base64.NO_WRAP)}")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@PaymentActivity, "Failed to create payment URL. ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)
                    val paymentUrl = jsonResponse.getString("redirect_url")
                    runOnUiThread {
                        Toast.makeText(this@PaymentActivity, "Payment URL: $paymentUrl", Toast.LENGTH_LONG).show()
                        val link: TextView = findViewById(R.id.welcome)
                        link.text = HtmlCompat.fromHtml("<a href=\"$paymentUrl\">Click here to pay</a>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                        link.movementMethod = android.text.method.LinkMovementMethod.getInstance()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@PaymentActivity, "Failed to create payment URL. ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}
