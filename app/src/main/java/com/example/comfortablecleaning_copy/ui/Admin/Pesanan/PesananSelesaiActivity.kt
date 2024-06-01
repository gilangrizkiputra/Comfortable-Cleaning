package com.example.comfortablecleaning_copy.ui.Admin.Pesanan

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
import com.example.comfortablecleaning_copy.ui.Admin.Pesanan.adaptor.AdaptorPesananSelesaiAdmin
import com.example.comfortablecleaning_copy.ui.Customer.Pesanan.Adaptor.AdaptorPesanan
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PesananSelesaiActivity : AppCompatActivity() {

    lateinit var rvPesananUser: RecyclerView
    lateinit var ivPesananSelesaiBack: ImageView
    lateinit var adaptorPesanan: AdaptorPesananSelesaiAdmin
    private lateinit var database: DatabaseReference
    private var arrayList: ArrayList<Pesanan> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesanan_selesai)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivPesananSelesaiBack = findViewById(R.id.iv_pesanan_selesai_back)
        ivPesananSelesaiBack.setOnClickListener {
            val intent = Intent(this, BerandaAdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        rvPesananUser = findViewById(R.id.rv_pesanan_admin_selesai)

        database = FirebaseDatabase.getInstance().getReference("pesanan")

        rvPesananUser.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
        showData()
    }

    private fun showData() {
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (item in snapshot.children){
                    val pesanan = item.getValue(Pesanan::class.java)
                    pesanan?.let {
                        if (it.status == "Selesai"){
                            arrayList.add(it)
                        }
                    }
                }
                adaptorPesanan = AdaptorPesananSelesaiAdmin(arrayList, this@PesananSelesaiActivity)
                rvPesananUser.adapter = adaptorPesanan
                adaptorPesanan.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}