package com.example.comfortablecleaning_copy.Admin.EditData.Cleaning

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.comfortablecleaning_copy.Admin.ListTerdaftar.ListTerdaftarActivity
import com.example.comfortablecleaning_copy.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class EditDataCleaningActivity : AppCompatActivity() {

    private lateinit var ivBackAdmin: ImageView
    private lateinit var edtEditJenis: EditText
    private lateinit var edtNamaProduk: EditText
    private lateinit var edtHarga: EditText
    private lateinit var edtEditEstimasi: EditText
    private lateinit var edtDeskripsi: EditText
    private lateinit var ivEditGambar: ImageView
    private lateinit var btnPilihGambar: ImageButton
    private lateinit var btnSimpan: Button
    private lateinit var idProduk: String
    private lateinit var jenis: String
    private lateinit var namaProduk: String
    private var harga: Int = 0
    private lateinit var estimasi: String
    private lateinit var deskripsi: String
    private lateinit var imageUrl: String
    private var fileUri: Uri? = null
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("admin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_data_cleaning)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivBackAdmin = findViewById(R.id.iv_back_admin_edit)
        edtEditJenis = findViewById(R.id.edt_edit_jenis)
        edtNamaProduk = findViewById(R.id.edt_edit_nama)
        edtHarga = findViewById(R.id.edt_edit_harga)
        edtEditEstimasi = findViewById(R.id.edt_edit_estimasi)
        edtDeskripsi = findViewById(R.id.edt_edit_deskripsi)
        ivEditGambar = findViewById(R.id.img_edit_data)
        btnPilihGambar = findViewById(R.id.btn_tambah_data_edit)
        btnSimpan = findViewById(R.id.btn_simpan_edit)

        // Check for intent extras and parse them correctly
        if (intent.hasExtra("idProduk") && intent.hasExtra("jenis") && intent.hasExtra("namaProduk") &&
            intent.hasExtra("harga") && intent.hasExtra("estimasi") && intent.hasExtra("deskripsi") && intent.hasExtra("imageUrl")) {

            idProduk = intent.getStringExtra("idProduk") ?: ""
            jenis = intent.getStringExtra("jenis") ?: ""
            namaProduk = intent.getStringExtra("namaProduk") ?: ""
            harga = intent.getIntExtra("harga", 0)
            estimasi = intent.getStringExtra("estimasi") ?: ""
            deskripsi = intent.getStringExtra("deskripsi") ?: ""
            imageUrl = intent.getStringExtra("imageUrl") ?: ""
        }

        // Set the EditTexts with the retrieved values
        edtEditJenis.setText(jenis)
        edtNamaProduk.setText(namaProduk)
        edtHarga.setText(harga.toString()) // Convert Integer to String
        edtEditEstimasi.setText(estimasi)
        edtDeskripsi.setText(deskripsi)

        // Set the existing image
        Picasso.get().load(imageUrl).into(ivEditGambar)

        btnPilihGambar.setOnClickListener {
            openGallery()
        }


        btnSimpan.setOnClickListener {
            val jenisBaru = edtEditJenis.text.toString()
            val namaProdukBaru = edtNamaProduk.text.toString()
            val hargaBaru = edtHarga.text.toString().toIntOrNull() ?: 0
            val estimasiBaru = edtEditEstimasi.text.toString()
            val deskripsiBaru = edtDeskripsi.text.toString()

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["jenis"] = jenisBaru
            hashMap["namaProduk"] = namaProdukBaru
            hashMap["harga"] = hargaBaru
            hashMap["estimasi"] = estimasiBaru
            hashMap["deskripsi"] = deskripsiBaru

            if (fileUri != null) {
                uploadImage(idProduk, hashMap)
            } else {
                updateData(hashMap)
            }
        }

        ivBackAdmin.setOnClickListener {
            val intent = Intent(this, ListTerdaftarActivity::class.java)
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
                ivEditGambar.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar untuk di upload"), 0)
    }

    private fun uploadImage(idProduk: String, hashMap: HashMap<String, Any>) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading Image...")
        progressDialog.setMessage("Processing...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val storageRef = FirebaseStorage.getInstance().getReference("produk/$idProduk.jpg")

        storageRef.putFile(fileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    hashMap["imageUrl"] = downloadUrl
                    updateData(hashMap)
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

    private fun updateData(hashMap: HashMap<String, Any>) {
        database.child(idProduk).updateChildren(hashMap).addOnSuccessListener {
            Toast.makeText(applicationContext, "Update Berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, BerandaAdminActivity::class.java))
            finish()
        }
    }
}
