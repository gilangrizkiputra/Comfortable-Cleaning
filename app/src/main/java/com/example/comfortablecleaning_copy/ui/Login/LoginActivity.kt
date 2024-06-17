package com.example.comfortablecleaning_copy.Login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.Register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnMasuk: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val REQUEST_LOGOUT = 123 // Kode permintaan unik untuk memulai aktivitas logout
        const val RESULT_LOGOUT = 124 // Kode hasil unik untuk menangani hasil dari aktivitas logout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)

        // Cek apakah pengguna sudah login sebelumnya
        if (isLoggedIn()) {
            // Jika sudah login, arahkan ke halaman sesuai peran
            redirectToAppropriateScreen()
            return
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
                        val userReference =
                            FirebaseDatabase.getInstance().getReference("users").child(userId.toString())
                        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val role = snapshot.child("role").getValue(String::class.java)

                                // Menyimpan status login setelah berhasil login
                                saveLoginStatus(role)

                                // Arahkan ke halaman sesuai peran
                                if (role == "admin") {
                                    Toast.makeText(
                                        applicationContext,
                                        "Login Berhasil sebagai Admin",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(applicationContext, BerandaAdminActivity::class.java))
                                } else {
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
                                Toast.makeText(
                                    applicationContext,
                                    "Error: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Email atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // menyimpan status login ke SharedPreferences
    private fun saveLoginStatus(role: String?) {
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", true)
            putString("userRole", role)
            apply()
        }
    }

    // cek apakah pengguna sudah login
    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    // mengarahkan pengguna ke halaman yang sesuai berdasarkan status login
    private fun redirectToAppropriateScreen() {
        val role = sharedPreferences.getString("userRole", "")

        if (role == "admin") {
            startActivity(Intent(applicationContext, BerandaAdminActivity::class.java))
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        finish()
    }

    // Fungsi untuk logout
    private fun logout() {
        with(sharedPreferences.edit()) {
            remove("isLoggedIn")
            remove("userRole")
            apply()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_LOGOUT)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGOUT && resultCode == RESULT_LOGOUT) {
            logout()
        }
    }
}