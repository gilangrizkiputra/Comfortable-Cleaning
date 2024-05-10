package com.example.comfortablecleaning_copy.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.Customer.Beranda.BerandaFragment
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.Register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvDaftar:TextView = findViewById(R.id.tv_daftar)
        tvDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnMasuk:Button = findViewById(R.id.btn_masuk)
        btnMasuk.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnGoogle:Button = findViewById(R.id.btn_google)
        btnGoogle.setOnClickListener {
            val intent = Intent(this, BerandaAdminActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}