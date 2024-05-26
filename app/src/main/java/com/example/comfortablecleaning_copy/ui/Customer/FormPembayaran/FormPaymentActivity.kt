package com.example.comfortablecleaning_copy.Customer.FormPembayaran

import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.R

class FormPaymentActivity : AppCompatActivity() {
    private lateinit var edtNama: EditText
    private lateinit var edtNoTelp: EditText
    private lateinit var edtAlamat: EditText
    private lateinit var edtCatatan: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var detailPesanan: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}