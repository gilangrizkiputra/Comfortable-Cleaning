package com.example.comfortablecleaning_copy.Register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtUsernameReg : EditText
    private lateinit var edtEmailReg : EditText
    private lateinit var edtPasswordReg : EditText
    private lateinit var edtPasswordKonfirmReg : EditText
    private lateinit var btnDaftar : Button

    //dekllarasi variabel koneksi firebase
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //panggil database
        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://comfortable-cleaning-default-rtdb.firebaseio.com/")

        edtUsernameReg = findViewById(R.id.edt_username_reg)
        edtEmailReg = findViewById(R.id.edt_email_reg)
        edtPasswordReg = findViewById(R.id.edt_kata_sandi_reg)
        edtPasswordKonfirmReg = findViewById(R.id.edt_konfirm_kata_sandi_reg)
        btnDaftar = findViewById(R.id.btn_daftar)

        val tvLogin: TextView = findViewById(R.id.tv_login)
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnDaftar.setOnClickListener {
            val username = edtUsernameReg.text.toString()
            val email = edtEmailReg.text.toString()
            val password = edtPasswordReg.text.toString()
            val passwordKonfirmasi = edtUsernameReg.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordKonfirmasi.isEmpty()){
                Toast.makeText(applicationContext, "Ada Data Yang Masih Kosong!!", Toast.LENGTH_SHORT).show()
            }else{
                database = FirebaseDatabase.getInstance().getReference("users")
                database.child(username).child("username").setValue(username)
                database.child(username).child("email").setValue(email)
                database.child(username).child("password").setValue(password)
                database.child(username).child("password_konfirmasi").setValue(passwordKonfirmasi)

                Toast.makeText(applicationContext, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                val register =Intent(applicationContext, LoginActivity::class.java)
                startActivity(register)
                finish()
            }
        }


    }
}