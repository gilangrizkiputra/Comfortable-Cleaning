package com.example.comfortablecleaning_copy.Admin.Pesanan

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.Customer.Pesanan.DetailPesanan.DetailPesananActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Admin.Pesanan.adaptor.AdaptorRvPesananAdmin
import com.example.comfortablecleaning_copy.ui.Customer.Pesanan.Adaptor.AdaptorPesanan
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.database.*

class PesananActivity : AppCompatActivity() {

    lateinit var rvPesananAdmin: RecyclerView
    lateinit var adaptorPesananAdmin: AdaptorRvPesananAdmin
    lateinit var ivBackPesananAdmin: ImageView
    private lateinit var database: DatabaseReference
    private var arrayList: ArrayList<Pesanan> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pesanan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivBackPesananAdmin = findViewById(R.id.iv_back_pesanan_admin)
        ivBackPesananAdmin.setOnClickListener {
            val intent = Intent(this, BerandaAdminActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        rvPesananAdmin = findViewById(R.id.rv_pesanan_admin)

        database = FirebaseDatabase.getInstance().getReference("pesanan")

        rvPesananAdmin.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }

        adaptorPesananAdmin = AdaptorRvPesananAdmin(arrayList, this@PesananActivity)

        rvPesananAdmin.adapter = adaptorPesananAdmin

        showData()
    }

    private fun showData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
                for (item in snapshot.children) {
                    val pesanan = item.getValue(Pesanan::class.java)
                    pesanan?.let {
                        if (it.status != "Selesai") {
                            arrayList.add(it)
                        }
                    }
                }
                adaptorPesananAdmin.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
