package com.example.comfortablecleaning_copy.Admin.EditData.Cleaning

import android.content.Intent
import android.os.Bundle
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
import com.example.comfortablecleaning_copy.Admin.ListTerdaftar.ListTerdaftarActivity
import com.example.comfortablecleaning_copy.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditDataCleaningActivity : AppCompatActivity() {

    private lateinit var ivBackAdmin: ImageView
    private lateinit var edtEditJenis: EditText
    private lateinit var edtNamaProduk: EditText
    private lateinit var edtHarga: EditText
    private lateinit var edtDeskripsi: EditText
    private lateinit var btnTambahDataEditText: ImageButton
    private lateinit var btnHapusDataEdit: ImageButton
    private lateinit var btnSimpan: Button
    private lateinit var jenis: String
    private lateinit var namaProduk: String
    private lateinit var harga: String
    private lateinit var deskripsi: String
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
        edtDeskripsi = findViewById(R.id.edt_edit_deskripsi)
        btnTambahDataEditText = findViewById(R.id.btn_tambah_data_edit)
        btnHapusDataEdit = findViewById(R.id.btn_hapus_data_edit)
        btnSimpan = findViewById(R.id.btn_simpan_edit)

        if (intent.hasExtra("jenis") && intent.hasExtra("namaProduk" ) && intent.hasExtra("harga" ) && intent.hasExtra("deskripsi")){
            jenis = intent.getStringExtra("jenis") ?: ""
            namaProduk = intent.getStringExtra("namaProduk") ?: ""
            harga = intent.getStringExtra("harga") ?: ""
            deskripsi = intent.getStringExtra("deskripsi") ?: ""

        }

        edtEditJenis.setText(jenis)
        edtNamaProduk.setText(namaProduk)
        edtHarga.setText(harga)
        edtDeskripsi.setText(deskripsi)

        btnSimpan.setOnClickListener {
            val jenisBaru = edtEditJenis.text.toString()
            val namaProdukBaru = edtNamaProduk.text.toString()
            val hargaBaru = edtHarga.text.toString()
            val deskripsiBaru = edtDeskripsi.text.toString()

            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["jenis"] = jenisBaru
            hashMap["namaProduk"] = namaProdukBaru
            hashMap["harga"] = hargaBaru
            hashMap["deskripsi"] = deskripsiBaru

            database.child(namaProduk).updateChildren(hashMap).addOnSuccessListener {
                Toast.makeText(applicationContext, "Update Berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, BerandaAdminActivity::class.java))
                finish()
            }
        }

        ivBackAdmin.setOnClickListener {
            val intent = Intent(this, ListTerdaftarActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}