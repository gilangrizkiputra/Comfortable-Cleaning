package com.example.comfortablecleaning_copy.Admin.BerandaAdmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.Admin.ListTerdaftar.ListTerdaftarActivity
import com.example.comfortablecleaning_copy.Admin.Pesanan.PesananActivity
import com.example.comfortablecleaning_copy.Admin.Profile.ProfileAdminActivity
import com.example.comfortablecleaning_copy.Admin.TambahCleaning.TambahCleaningActivity
import com.example.comfortablecleaning_copy.Customer.Beranda.BerandaFragment
import com.example.comfortablecleaning_copy.Customer.Profile.ProfileFragment
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.R

class BerandaAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beranda_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pesananAdmin : LinearLayout = findViewById(R.id.pesanan_admin)
        val pesananSelesaiAdmin : LinearLayout = findViewById(R.id.pesanan_selesai_admin)
        val tambahCleaning : CardView = findViewById(R.id.cv_tambah_cleaning)
        val listTerdaftar : CardView = findViewById(R.id.cv_list_terdaftar)
        val repaint : CardView = findViewById(R.id.cv_repaint)
        val profileAdmin : CardView = findViewById(R.id.cv_profile_admin)
        val btnKeluarAdmin : Button = findViewById(R.id.btn_keluar_admin)

        pesananAdmin.setOnClickListener {
            val intent = Intent(this, PesananActivity::class.java)
            startActivity(intent)
        }

        pesananSelesaiAdmin.setOnClickListener {
            val intent = Intent(this, PesananActivity::class.java)
            startActivity(intent)
        }

        tambahCleaning.setOnClickListener {
            val intent = Intent(this, TambahCleaningActivity::class.java)
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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}