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
import com.google.firebase.auth.FirebaseAuth
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

    //aunthentification
    private lateinit var auth : FirebaseAuth

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
        //database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://comfortable-cleaning-default-rtdb.firebaseio.com/")

        auth = FirebaseAuth.getInstance()

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
            val passwordKonfirmasi = edtPasswordKonfirmReg.text.toString()

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmailReg.error = "Email tidak valid"
            }

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordKonfirmasi.isEmpty()) {
                Toast.makeText(applicationContext, "Ada Data yang masih kosong", Toast.LENGTH_SHORT).show()
            } else if (password.length <= 8) {
                edtPasswordReg.setError("Password harus lebih dari 8 karakter")
            } else if (!passwordKonfirmasi.equals(password)) {
                edtPasswordKonfirmReg.setError("Password tidak sama")
            } else {
                // Membuat pengguna di Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Pengguna berhasil dibuat
                            val currentUser = auth.currentUser
                            currentUser?.let { user ->
                                // Mengambil ID pengguna dari autentikasi
                                val userId = user.uid
                                // Menyimpan informasi pengguna ke Realtime Database
                                val userReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
                                val userData = HashMap<String, String>()
                                userData["username"] = username
                                userData["email"] = email
                                userData["role"] = "user" // Menetapkan peran pengguna secara otomatis
                                userReference.setValue(userData)
                                    .addOnCompleteListener { databaseTask ->
                                        if (databaseTask.isSuccessful) {
                                            // Berhasil menyimpan data pengguna ke Realtime Database
                                            // Kirim email verifikasi
                                            user.sendEmailVerification().addOnCompleteListener { verificationTask ->
                                                if (verificationTask.isSuccessful) {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Daftar Berhasil. Silakan verifikasi email Anda, cek di folder Spam",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                                                    finish()
                                                } else {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Gagal mengirim email verifikasi.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } else {
                                            // Gagal menyimpan data pengguna ke Realtime Database
                                            Toast.makeText(
                                                applicationContext,
                                                "Gagal mendaftar. Mohon coba lagi.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                        } else {
                            // Gagal membuat pengguna di Firebase Authentication
                            Toast.makeText(
                                applicationContext,
                                "Gagal mendaftar. Mohon coba lagi.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}