package com.example.comfortablecleaning_copy.Admin.Profile

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.Customer.Profile.BantuanDanLaporan.BantuanDanLaporanActivity
import com.example.comfortablecleaning_copy.Customer.Profile.GantiKataSandi.GantiKataSandiActivity
import com.example.comfortablecleaning_copy.Customer.Profile.TentangKami.TentangKamiActivity
import com.example.comfortablecleaning_copy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProfileAdminActivity : AppCompatActivity() {

    private lateinit var tvUsernameAdmin: TextView
    private lateinit var tvEmailAdmin: TextView
    private lateinit var UbahFotoProfil: LinearLayout
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var fileUri: Uri? = null
    private lateinit var imageViewProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvUsernameAdmin = findViewById(R.id.tv_username_admin_profile)
        tvEmailAdmin = findViewById(R.id.tv_email_admin)
        UbahFotoProfil = findViewById(R.id.ubah_foto_profil_admin)
        imageViewProfile = findViewById(R.id.iv_foto_profile_admin)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance().getReference("users/$userId")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val namaAdmin = snapshot.child("username").value.toString()
                    tvUsernameAdmin.text = namaAdmin
                    val emailAdmin = snapshot.child("email").value.toString()
                    tvEmailAdmin.text = emailAdmin
                    if (snapshot.hasChild("profileImageUrl")) {
                        val profileImageUrl = snapshot.child("profileImageUrl").value.toString()
                        if (profileImageUrl.isNotEmpty()) {
                            Glide.with(this@ProfileAdminActivity).load(profileImageUrl)
                                .into(imageViewProfile)
                        } else {
                            imageViewProfile.setImageResource(R.drawable.image_profil)
                        }
                    } else {
                        imageViewProfile.setImageResource(R.drawable.image_profil)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())

            }
        })

        UbahFotoProfil.setOnClickListener {
            openGallery()
        }

        val gantiKataSandi: LinearLayout = findViewById(R.id.ganti_kata_sandi)
        val tentangKami: LinearLayout = findViewById(R.id.tentang_kami)
        val bantuanDanLaporan: LinearLayout = findViewById(R.id.bantuan_dan_laporan)
        val btnKembaliBeranda: Button = findViewById(R.id.btn_kembali_beranda_admin)

        gantiKataSandi.setOnClickListener {
            val intent = Intent(this, GantiKataSandiActivity::class.java)
            startActivity(intent)
        }

        tentangKami.setOnClickListener {
            val intent = Intent(this, TentangKamiActivity::class.java)
            startActivity(intent)
        }

        bantuanDanLaporan.setOnClickListener {
            val intent = Intent(this, BantuanDanLaporanActivity::class.java)
            startActivity(intent)
        }

        btnKembaliBeranda.setOnClickListener {
            val intent = Intent(this, BerandaAdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                imageViewProfile.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Exception", "Error : $e")
            }
            uploadImage()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar untuk di upload"), 0)
    }

    private fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading Image...")
            progressDialog.setMessage("Processing...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid ?: ""

            val storageRef = FirebaseStorage.getInstance().getReference("users/$userId.jpg")

            storageRef.putFile(fileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Berhasil mengupload gambar profil", Toast.LENGTH_SHORT).show()

                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        database.child("profileImageUrl").setValue(downloadUrl)
                            .addOnSuccessListener {
                                Log.d("ProfileImageUrl", "URL gambar berhasil disimpan di Realtime Database")
                            }
                            .addOnFailureListener { e ->
                                Log.e("ProfileImageUrl", "Gagal menyimpan URL gambar di Realtime Database: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Gagal mengupload gambar profil: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    progressDialog.dismiss()
                }
        }
    }
}