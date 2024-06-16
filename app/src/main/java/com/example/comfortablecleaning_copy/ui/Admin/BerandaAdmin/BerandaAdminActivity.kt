package com.example.comfortablecleaning_copy.Admin.BerandaAdmin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.comfortablecleaning_copy.Admin.ListTerdaftar.ListTerdaftarActivity
import com.example.comfortablecleaning_copy.Admin.Pesanan.PesananActivity
import com.example.comfortablecleaning_copy.Admin.Profile.ProfileAdminActivity
import com.example.comfortablecleaning_copy.Admin.TambahCleaning.TambahItemActivity
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Admin.Pesanan.PesananSelesaiActivity
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.example.comfortablecleaning_copy.ui.Entitas.Produk
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BerandaAdminActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tvNamaAdmin: TextView
    private lateinit var ivGambarProfilAdmin : ImageView
    private lateinit var tvJumlahPesanan: TextView
    private lateinit var tvJumlahPesananSelesai: TextView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beranda_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("login_status", Context.MODE_PRIVATE)
        tvNamaAdmin = findViewById(R.id.tv_nama_admin)
        ivGambarProfilAdmin = findViewById(R.id.iv_gambar_profile_beranda_admin)
        tvJumlahPesanan = findViewById(R.id.tv_jumlah_pesanan)
        tvJumlahPesananSelesai = findViewById(R.id.tv_jumlah_pesanan_selesai)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance().getReference("users/$userId")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val namaAdmin = snapshot.child("username").value.toString()
                    tvNamaAdmin.text = namaAdmin

                    // Mendapatkan profileImageUrl dari Realtime Database
                    val profileImageUrl = snapshot.child("profileImageUrl").value.toString()

                    // Menampilkan gambar profil menggunakan Glide atau library lain
                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(this@BerandaAdminActivity)
                            .load(profileImageUrl)
                            .into(ivGambarProfilAdmin)
                    } else {
                        // Tampilkan gambar default jika tidak ada gambar profil
                        ivGambarProfilAdmin.setImageResource(R.drawable.image_profil)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }
        })

        // Mengambil jumlah pesanan dari Firebase
        val pesananRef = FirebaseDatabase.getInstance().getReference("pesanan")
        pesananRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var totalPesanan = 0
                    var totalPesananSelesai = 0
                    for (item in snapshot.children) {
                        val pesanan = item.getValue(Pesanan::class.java)
                        pesanan?.let {
                            totalPesanan++
                            if (it.status == "Selesai") {
                                totalPesananSelesai++
                                totalPesanan--
                            }
                        }
                    }
                    tvJumlahPesanan.text = "Pesanan : $totalPesanan"
                    tvJumlahPesananSelesai.text = "Selesai : $totalPesananSelesai"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }
        })

        val pesananAdmin: LinearLayout = findViewById(R.id.pesanan_admin)
        val pesananSelesaiAdmin: LinearLayout = findViewById(R.id.pesanan_selesai_admin)
        val tambahCleaning: CardView = findViewById(R.id.cv_tambah_cleaning)
        val listTerdaftar: CardView = findViewById(R.id.cv_list_terdaftar)
        val profileAdmin: CardView = findViewById(R.id.cv_profile_admin)
        val btnKeluarAdmin: Button = findViewById(R.id.btn_keluar_admin)

        val selectedData = intent.getParcelableExtra<Produk>("selectedData")

        pesananAdmin.setOnClickListener {
            val intent = Intent(this, PesananActivity::class.java)
            intent.putExtra("selectedData", selectedData)
            intent.putExtra("jenis", selectedData?.jenis)
            startActivity(intent)
        }

        pesananSelesaiAdmin.setOnClickListener {
            val intent = Intent(this, PesananSelesaiActivity::class.java)
            startActivity(intent)
        }

        tambahCleaning.setOnClickListener {
            val intent = Intent(this, TambahItemActivity::class.java)
            startActivity(intent)
        }

        listTerdaftar.setOnClickListener {
            val intent = Intent(this, ListTerdaftarActivity::class.java)
            startActivity(intent)
        }

        profileAdmin.setOnClickListener {
            val intent = Intent(this, ProfileAdminActivity::class.java)
            startActivity(intent)
        }

        btnKeluarAdmin.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        with(sharedPreferences.edit()) {
            remove("isLoggedIn")
            remove("userRole")
            apply()
        }
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}