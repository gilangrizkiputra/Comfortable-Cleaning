package com.example.comfortablecleaning_copy.Admin.TambahCleaning

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class TambahItemActivity : AppCompatActivity() {

    private lateinit var edtjenis: EditText
    private lateinit var edtNamaProduk: EditText
    private lateinit var edtHarga: EditText
    private lateinit var edtEstimasi: EditText
    private lateinit var edtDeskripsi: EditText
    private lateinit var btnTambahkan: Button
    private lateinit var ivBackAdmin: ImageView

    private lateinit var btnPilihGambar: ImageButton
    private lateinit var imageView: ImageView
    private var fileUri: Uri? = null

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtjenis = findViewById(R.id.edt_jenis)
        edtNamaProduk = findViewById(R.id.edt_nama_cleaning)
        edtHarga = findViewById(R.id.edt_harga)
        edtEstimasi = findViewById(R.id.edt_estimasi)
        edtDeskripsi = findViewById(R.id.edt_deskripsi)
        btnTambahkan = findViewById(R.id.btn_tambah)
        ivBackAdmin = findViewById(R.id.iv_back_admin)

        btnPilihGambar = findViewById(R.id.btn_up_image)
        imageView = findViewById(R.id.imageView)

        database = FirebaseDatabase.getInstance().getReferenceFromUrl("https://comfortable-cleaning-default-rtdb.firebaseio.com/")

        btnTambahkan.setOnClickListener {
            val jenis = edtjenis.text.toString()
            val namaProduk = edtNamaProduk.text.toString()
            val hargaText = edtHarga.text.toString()
            val estimasi = edtEstimasi.text.toString()
            val deskripsi = edtDeskripsi.text.toString()

            if (jenis.isEmpty() || namaProduk.isEmpty() || hargaText.isEmpty() || deskripsi.isEmpty() || fileUri == null) {
                Toast.makeText(applicationContext, "Ada Data Yang Masih Kosong!!", Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext, "Tolong pilih gambar jika ingin mengupload", Toast.LENGTH_SHORT).show()
            } else {
                val harga: Int? = hargaText.toIntOrNull()
                if (harga == null) {
                    Toast.makeText(applicationContext, "Harga harus berupa angka", Toast.LENGTH_SHORT).show()
                } else {
                    // id random
                    val idProduk = UUID.randomUUID().toString()

                    val produkMap = HashMap<String, Any>()
                    produkMap["idProduk"] = idProduk
                    produkMap["jenis"] = jenis
                    produkMap["namaProduk"] = namaProduk
                    produkMap["harga"] = harga
                    produkMap["estimasi"] = estimasi
                    produkMap["deskripsi"] = deskripsi

                    database = FirebaseDatabase.getInstance().getReference("admin").child(idProduk)
                    database.setValue(produkMap)
                        .addOnSuccessListener {
                            uploadImage(idProduk)
                            Toast.makeText(applicationContext, "Berhasil menambahkan Item", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(applicationContext, "Gagal menambahkan Item: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        btnPilihGambar.setOnClickListener {
            openGallery()
        }

        ivBackAdmin.setOnClickListener {
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
                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Exception", "Error : " + e)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar untuk di upload"), 0)
    }

    private fun uploadImage(idProduk: String) {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading Image...")
            progressDialog.setMessage("Processing...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val storageRef = FirebaseStorage.getInstance().getReference("admin/$idProduk.jpg")

            storageRef.putFile(fileUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Berhasil mengupload gambar", Toast.LENGTH_SHORT).show()

                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        database.child("imageUrl").setValue(downloadUrl)
                            .addOnSuccessListener {
                                Log.d("imageUrl", "URL gambar berhasil disimpan di Realtime Database admin")
                            }
                            .addOnFailureListener { e ->
                                Log.e("imageUrl", "Gagal menyimpan URL gambar di Realtime Database admin: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "Gagal mengupload gambar: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    progressDialog.dismiss()
                }
        }
    }
}
