package com.example.comfortablecleaning_copy.Admin.ListTerdaftar

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Admin.ListTerdaftar.AdaptorListTerdaftarAdmin
import com.example.comfortablecleaning_copy.ui.Entitas.Produk
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListTerdaftarActivity : AppCompatActivity() {

    private lateinit var ivBackListTerdaftar : ImageView
    private lateinit var rvListTerdaftarAdmin : RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var adaptorListTerdaftarAdmin: AdaptorListTerdaftarAdmin
    private var arrayList: ArrayList<Produk> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_terdaftar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivBackListTerdaftar = findViewById(R.id.iv_back_list_terdaftar)
        rvListTerdaftarAdmin = findViewById(R.id.rv_list_terdaftar_admin)

        ivBackListTerdaftar.setOnClickListener {
            val intent = Intent(this, BerandaAdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        database = FirebaseDatabase.getInstance().getReference("produk")

        rvListTerdaftarAdmin.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListTerdaftarActivity)
            itemAnimator = DefaultItemAnimator()
        }

        ShowData()


    }

    private fun ShowData() {
        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (item in snapshot.children){
                    val produk = Produk()
                    produk.idProduk = item.child("idProduk").getValue(String::class.java)
                    produk.jenis = item.child("jenis").getValue(String::class.java)
                    produk.namaProduk = item.child("namaProduk").getValue(String::class.java)
                    produk.harga = item.child("harga").getValue(Int::class.java)
                    produk.estimasi = item.child("estimasi").getValue(String::class.java)
                    produk.deskripsi = item.child("deskripsi").getValue(String::class.java)
                    produk.imageUrl = item.child("imageUrl").getValue(String::class.java)
                    arrayList.add(produk)
                }

                adaptorListTerdaftarAdmin = AdaptorListTerdaftarAdmin(arrayList, this@ListTerdaftarActivity)
                rvListTerdaftarAdmin.adapter = adaptorListTerdaftarAdmin
                adaptorListTerdaftarAdmin.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}