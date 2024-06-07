//package com.example.comfortablecleaning_copy
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.comfortablecleaning_copy.databinding.ActivityCobaMidtransBinding
//import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback
//import com.midtrans.sdk.corekit.core.MidtransSDK
//import com.midtrans.sdk.corekit.core.TransactionRequest
//import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
//import com.midtrans.sdk.corekit.models.*
//import com.midtrans.sdk.corekit.models.snap.TransactionResult
//import com.midtrans.sdk.uikit.SdkUIFlowBuilder
//
//class CobaMidtrans : AppCompatActivity(), TransactionFinishedCallback {
//
//    private lateinit var binding: ActivityCobaMidtransBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCobaMidtransBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        initMidtransSDK()
//
//        binding.btnKitabTs.setOnClickListener { goToPayment() }
//    }
//
//    private fun initMidtransSDK() {
//        SdkUIFlowBuilder.init()
//            .setClientKey("SB-Mid-client-5KgpjvD2Gugqi2Gy") // client_key
//            .setContext(this)
//            .setTransactionFinishedCallback(this)
//            .setMerchantBaseUrl("http://cleancomfortable.my.id/midtrans_charge.php/") // URL Server
//            .enableLog(true)
//            .setColorTheme(CustomColorTheme("#002855", "#FF6200EE", "#FF018786"))
//            .setLanguage("id")
//            .buildSDK()
//
//        Log.d("Midtrans", "Midtrans SDK initialized successfully")
//    }
//
//    private fun goToPayment() {
//        val qty = 2
//        val harga = 95000.0
//        val amount = qty * harga
//
//        val transactionRequest = TransactionRequest("Hammad-StoreID-${System.currentTimeMillis()}", amount)
//        val itemDetails = arrayListOf(ItemDetails("BUKU_101", harga, qty, "Tadzkirotus Sami"))
//
//        transactionRequest.itemDetails = itemDetails
//        uiKitDetails(transactionRequest)
//
//        Log.d("Midtrans", "Transaction Request: $transactionRequest")
//
//        MidtransSDK.getInstance().transactionRequest = transactionRequest
//        MidtransSDK.getInstance().startPaymentUiFlow(this)
//
//        Log.d("Midtrans", "Started payment UI flow")
//    }
//
//    private fun uiKitDetails(transactionRequest: TransactionRequest) {
//        val customerDetails = CustomerDetails().apply {
//            customerIdentifier = "Asep Sutisna"
//            phone = "081775205889"
//            firstName = "Asep"
//            lastName = "Sutisna"
//            email = "yummi2102@gmail.com"
//            shippingAddress = ShippingAddress().apply {
//                address = "cikeas, Nagrak"
//                city = "Bogor"
//                postalCode = "16967"
//            }
//            billingAddress = BillingAddress().apply {
//                address = "Cikeas, Nagrak"
//                city = "Bogor"
//                postalCode = "16967"
//            }
//        }
//
//        transactionRequest.customerDetails = customerDetails
//
//        Log.d("Midtrans", "Customer Details: $customerDetails")
//    }
//
//    override fun onTransactionFinished(result: TransactionResult) {
//        Log.d("Midtrans", "Transaction Result: $result")
//
//        if (result.response != null) {
//            when (result.status) {
//                TransactionResult.STATUS_SUCCESS -> Toast.makeText(
//                    this,
//                    "Transaction Finished. ID: ${result.response?.transactionId}",
//                    Toast.LENGTH_LONG
//                ).show()
//                TransactionResult.STATUS_PENDING -> Toast.makeText(
//                    this,
//                    "Transaction Pending. ID: ${result.response?.transactionId}",
//                    Toast.LENGTH_LONG
//                ).show()
//                TransactionResult.STATUS_FAILED -> Toast.makeText(
//                    this,
//                    "Transaction Failed. ID: ${result.response?.transactionId}. Message: ${result.response?.statusMessage}",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        } else if (result.isTransactionCanceled) {
//            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show()
//        } else if (result.status.equals(TransactionResult.STATUS_INVALID, true)) {
//            Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show()
//        }
//    }
//}
