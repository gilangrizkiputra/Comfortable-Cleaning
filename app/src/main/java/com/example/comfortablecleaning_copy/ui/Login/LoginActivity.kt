package com.example.comfortablecleaning_copy.Login

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
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.Customer.Beranda.BerandaFragment
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.Register.RegisterActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var edtUsername : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnMasuk : Button

    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtUsername = findViewById(R.id.edt_username)
        edtPassword = findViewById(R.id.edt_password)
        btnMasuk = findViewById(R.id.btn_masuk)



        val tvDaftar:TextView = findViewById(R.id.tv_daftar)
        tvDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }


        btnMasuk.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            database = FirebaseDatabase.getInstance().getReference("users")
            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(applicationContext, "Username atau Password Salah!", Toast.LENGTH_SHORT).show()
            }else  {
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(username).exists()){
                            if (snapshot.child(username).child("password").getValue(String::class.java) == password){
                                Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT).show()
                                val masuk = Intent(applicationContext, MainActivity::class.java)
                                startActivity(masuk)
                            }
                        }else {
                            Toast.makeText(applicationContext, "Data belum terdaftar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

        val btnGoogle:Button = findViewById(R.id.btn_google)
        btnGoogle.setOnClickListener {
            val intent = Intent(this, BerandaAdminActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}