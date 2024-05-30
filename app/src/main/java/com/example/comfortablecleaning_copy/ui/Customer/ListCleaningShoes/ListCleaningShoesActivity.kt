package com.example.comfortablecleaning_copy.Customer.ListCleaningShoes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.Customer.Beranda.BerandaFragment
import com.example.comfortablecleaning_copy.Customer.DetailCleaning.DetailCleaningActivity
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Admin.ListTerdaftar.AdaptorListTerdaftarAdmin
import com.example.comfortablecleaning_copy.ui.Customer.ListCleaningShoes.AdaptorListCleaningShoes
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListCleaningShoesActivity : AppCompatActivity() {

    private lateinit var rvListCleaning : RecyclerView
    private lateinit var backButton: ImageView
    private lateinit var database: DatabaseReference
    private lateinit var adaptorListCleaningShoes: AdaptorListCleaningShoes
    private var arrayList: ArrayList<Admin> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_cleaning_shoes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        backButton = findViewById(R.id.iv_back_beranda)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        rvListCleaning = findViewById(R.id.rv_list_cleaning)
        rvListCleaning.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ListCleaningShoesActivity)
            itemAnimator = DefaultItemAnimator()
        }

        adaptorListCleaningShoes = AdaptorListCleaningShoes(arrayList, this)
        adaptorListCleaningShoes.setOnItemClickListener(object : AdaptorListCleaningShoes.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val selectedData = arrayList[position]
                val intent = Intent(this@ListCleaningShoesActivity, DetailCleaningActivity::class.java)
                intent.putExtra("selectedData", selectedData)
                startActivity(intent)
            }
        })
        rvListCleaning.adapter = adaptorListCleaningShoes
        database = FirebaseDatabase.getInstance().getReference("admin")
        ShowData()
    }

//    private fun ShowData() {
//        database.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                arrayList.clear()
//                for (item in snapshot.children){
//                    val admin = Admin()
//                    admin.idProduk = item.child("idProduk").getValue(String::class.java)
//                    admin.jenis = item.child("jenis").getValue(String::class.java)
//                    admin.namaProduk = item.child("namaProduk").getValue(String::class.java)
//                    admin.harga = item.child("harga").getValue(String::class.java)
//                    admin.estimasi = item.child("estimasi").getValue(String::class.java)
//                    admin.deskripsi = item.child("deskripsi").getValue(String::class.java)
//                    admin.imageUrl = item.child("imageUrl").getValue(String::class.java)
//                    arrayList.add(admin)
//                }
//
//                adaptorListCleaningShoes = AdaptorListCleaningShoes(arrayList, this@ListCleaningShoesActivity)
//                rvListCleaning.adapter = adaptorListCleaningShoes
//                adaptorListCleaningShoes.notifyDataSetChanged()
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
    private fun ShowData() {
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (item in snapshot.children) {
                    val admin = item.getValue(Admin::class.java)
                    if (admin != null) {
                        arrayList.add(admin)
                    }
                }
                adaptorListCleaningShoes.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}