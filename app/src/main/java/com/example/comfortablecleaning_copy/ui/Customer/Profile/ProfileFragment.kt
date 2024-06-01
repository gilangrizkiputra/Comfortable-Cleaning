package com.example.comfortablecleaning_copy.Customer.Profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.comfortablecleaning_copy.Customer.Profile.BantuanDanLaporan.BantuanDanLaporanActivity
import com.example.comfortablecleaning_copy.Customer.Profile.GantiKataSandi.GantiKataSandiActivity
import com.example.comfortablecleaning_copy.Customer.Profile.TentangKami.TentangKamiActivity
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException

class ProfileFragment : Fragment() {

    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ubahFotoProfil: LinearLayout
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var selectedFileUri: Uri? = null
    private lateinit var imageViewProfile: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("login_status", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvUsername = view.findViewById(R.id.tv_username)
        tvEmail = view.findViewById(R.id.tv_email)
        ubahFotoProfil = view.findViewById(R.id.ubah_foto_profil)
        imageViewProfile = view.findViewById(R.id.iv_foto_profile)

        // Inisialisasi Firebase
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance().getReference("users/$userId")

        // Mengambil data pengguna dari Realtime Database
        loadUserData()

        ubahFotoProfil.setOnClickListener {
            openGallery()
        }

        val gantiKataSandi: LinearLayout = view.findViewById(R.id.ganti_kata_sandi)
        val tentangKami: LinearLayout = view.findViewById(R.id.tentang_kami)
        val BantuanDanLaporan: LinearLayout = view.findViewById(R.id.bantuan_dan_laporan)
        val btnKeluar: Button = view.findViewById(R.id.btn_keluar)

        gantiKataSandi.setOnClickListener {
            val intent = Intent(activity, GantiKataSandiActivity::class.java)
            startActivity(intent)
        }

        tentangKami.setOnClickListener {
            val intent = Intent(activity, TentangKamiActivity::class.java)
            startActivity(intent)
        }

        BantuanDanLaporan.setOnClickListener {
            val intent = Intent(activity, BantuanDanLaporanActivity::class.java)
            startActivity(intent)
        }

        btnKeluar.setOnClickListener {
            logout()
        }

        return view
    }

    private fun loadUserData() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.child("username").value.toString()
                    tvUsername.text = username
                    val email = snapshot.child("email").value.toString()
                    tvEmail.text = email

                    // Mengambil URL gambar profil dari Realtime Database
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java) ?: ""

                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(this@ProfileFragment).load(profileImageUrl).into(imageViewProfile)
                    } else {
                        imageViewProfile.setImageResource(R.drawable.image_profil)
                    }
                } else {
                    // Jika snapshot tidak ada, setel gambar profil ke gambar default
                    imageViewProfile.setImageResource(R.drawable.image_profil)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani error jika terjadi
                Log.e("DatabaseError", error.message)
            }
        })
    }

    private fun logout() {
        with(sharedPreferences.edit()) {
            remove("isLoggedIn")
            remove("userRole")
            apply()
        }
        startActivity(Intent(requireActivity(), LoginActivity::class.java))
        requireActivity().finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedFileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedFileUri)
                imageViewProfile.setImageBitmap(bitmap)
                uploadImage()
            } catch (e: IOException) {
                Log.e("Exception", "Error : $e")
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar untuk di upload"), 0)
    }

    private fun uploadImage() {
        if (selectedFileUri != null) {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setTitle("Uploading Image...")
            progressDialog.setMessage("Processing...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser
            val userId = currentUser?.uid ?: ""

            val storageRef = FirebaseStorage.getInstance().getReference("users/$userId.jpg")

            storageRef.putFile(selectedFileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Berhasil mengupload gambar profil", Toast.LENGTH_SHORT).show()

                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        database.child("profileImageUrl").setValue(downloadUrl)
                            .addOnSuccessListener {
                                Log.d("ProfileImageUrl", "URL gambar berhasil disimpan di Realtime Database")
                                // Memuat ulang gambar profil setelah berhasil diupload
                                Glide.with(this@ProfileFragment).load(downloadUrl).into(imageViewProfile)
                            }
                            .addOnFailureListener { e ->
                                Log.e("ProfileImageUrl", "Gagal menyimpan URL gambar di Realtime Database: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Gagal mengupload gambar profil: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}