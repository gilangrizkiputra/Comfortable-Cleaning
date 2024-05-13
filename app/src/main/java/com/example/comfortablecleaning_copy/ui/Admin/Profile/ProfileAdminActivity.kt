package com.example.comfortablecleaning_copy.Admin.Profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.Customer.Profile.BantuanDanLaporan.BantuanDanLaporanActivity
import com.example.comfortablecleaning_copy.Customer.Profile.GantiKataSandi.GantiKataSandiActivity
import com.example.comfortablecleaning_copy.Customer.Profile.TentangKami.TentangKamiActivity
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.R

class ProfileAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ubahFotoProfile : LinearLayout = findViewById(R.id.ubah_foto_profil)
        val gantiKataSandi : LinearLayout = findViewById(R.id.ganti_kata_sandi)
        val tentangKami : LinearLayout = findViewById(R.id.tentang_kami)
        val BantuanDanLaporan : LinearLayout = findViewById(R.id.bantuan_dan_laporan)
        val btnKeluar : Button = findViewById(R.id.btn_keluar)

        gantiKataSandi.setOnClickListener {
            val intent = Intent(this, GantiKataSandiActivity::class.java)
            startActivity(intent)
        }

        tentangKami.setOnClickListener {
            val intent = Intent(this, TentangKamiActivity::class.java)
            startActivity(intent)
        }

        BantuanDanLaporan.setOnClickListener {
            val intent = Intent(this, BantuanDanLaporanActivity::class.java)
            startActivity(intent)
        }

        btnKeluar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}