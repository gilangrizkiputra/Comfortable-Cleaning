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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnMasuk: Button

    private lateinit var database : DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnMasuk = findViewById(R.id.btn_masuk)

        auth = FirebaseAuth.getInstance()

        val tvDaftar: TextView = findViewById(R.id.tv_daftar)
        tvDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnMasuk.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Email atau password tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val userReference = FirebaseDatabase.getInstance().getReference("users").child(
                            userId.toString())
                        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val role = snapshot.child("role").getValue(String::class.java)
                                if (role == "admin") {
                                    // Jika pengguna adalah admin, arahkan mereka ke halaman admin
                                    Toast.makeText(
                                        applicationContext,
                                        "Login Berhasil sebagai Admin",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(applicationContext, BerandaAdminActivity::class.java))
                                } else {
                                    // Jika pengguna bukan admin, arahkan mereka ke halaman biasa
                                    Toast.makeText(
                                        applicationContext,
                                        "Login Berhasil sebagai User",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(applicationContext, MainActivity::class.java))
                                }
                                finish()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Penanganan kesalahan
                                Toast.makeText(
                                    applicationContext,
                                    "Error: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    } else {
                        // Penanganan kesalahan saat masuk
                        Toast.makeText(
                            applicationContext,
                            "Email atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

//        btnMasuk.setOnClickListener {
//            val email = edtEmail.text.toString()
//            val password = edtPassword.text.toString()
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(
//                    applicationContext,
//                    "Email atau password tidak boleh kosong",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else {
//                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        if (auth.currentUser?.isEmailVerified == true) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Login Berhasil",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            startActivity(Intent(applicationContext, MainActivity::class.java))
//                            finish()
//                        } else {
//                            edtEmail.setError("Email belum diverifikasi")
//                        }
//                    } else {
//                        Toast.makeText(
//                            applicationContext,
//                            "Email atau password salah",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            }
//        }
    }
}