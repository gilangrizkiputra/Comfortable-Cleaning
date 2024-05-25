package com.example.comfortablecleaning_copy.Customer.Profile.GantiKataSandi

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GantiKataSandiActivity : AppCompatActivity() {

    private  lateinit var edtPasswordLama : EditText
    private  lateinit var edtPasswordBaru : EditText
    private  lateinit var edtKonfirmasiPasswordBaru : EditText
    private lateinit var btnUbanKataSandi : Button

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ganti_kata_sandi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?:""
        database = FirebaseDatabase.getInstance().getReference("users/$userId")

        edtPasswordLama = findViewById(R.id.edt_password_lama)
        edtPasswordBaru = findViewById(R.id.edt_password_baru)
        edtKonfirmasiPasswordBaru = findViewById(R.id.edt_konfirm_password_baru)

        btnUbanKataSandi = findViewById(R.id.btn_ubah_password)
        btnUbanKataSandi.setOnClickListener {
            gantiKataSandi()
        }
    }

    private fun gantiKataSandi() {
        val passwordLama = edtPasswordLama.text.toString().trim()
        val passwordBaru = edtPasswordBaru.text.toString().trim()
        val konfirmasiPasswordBaru = edtKonfirmasiPasswordBaru.text.toString().trim()

        // Cek apakah password baru dan konfirmasi password baru sama
        if (passwordBaru == konfirmasiPasswordBaru) {
            val user = auth.currentUser

            // Lakukan re-autentikasi menggunakan password lama
            val credential = EmailAuthProvider.getCredential(user?.email!!, passwordLama)
            user?.reauthenticateAndRetrieveData(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Ganti kata sandi dengan password baru
                    user.updatePassword(passwordBaru)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "Berhasil mengganti kata sandi", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "Gagal mengganti kata sandi", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Perhatian")
                    builder.setMessage("Password baru dan konfirmasi password baru tidak sama")
                    builder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Perhatian")
            builder.setMessage("Password baru dan konfirmasi password baru tidak sama")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}