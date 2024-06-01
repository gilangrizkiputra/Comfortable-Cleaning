package com.example.comfortablecleaning_copy.Admin.DetailPesananAdmin

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.Admin.BerandaAdmin.BerandaAdminActivity
import com.example.comfortablecleaning_copy.Admin.Pesanan.PesananActivity
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan

class DetailPesananAdminActivity : AppCompatActivity() {

    private lateinit var tvDetailPesananAdmin: TextView
    private lateinit var ivBackDetailPesananAdmin : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_pesanan_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ivBackDetailPesananAdmin = findViewById(R.id.iv_back_detail_pesanan_admin)
        ivBackDetailPesananAdmin.setOnClickListener {
            val intent = Intent(this, PesananActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        val pesananData = intent.getParcelableExtra<Pesanan>("PESANAN_DATA")
        tvDetailPesananAdmin = findViewById(R.id.tv_detail_pesanan_admin)

        val detailPesananUserAdmin = "Nama: ${pesananData?.namaPemesan ?: ""}\n" +
                "Pesanan: ${pesananData?.namaProduk ?: ""}\n" +
                "Qty: ${pesananData?.quantity ?: 0}\n" +
                "Nomor Telepon: ${pesananData?.noTelpPemesan ?: ""}\n" +
                "Daerah: ${pesananData?.daerahPemesan ?: ""}\n" +
                "Alamat: ${pesananData?.alamatPemesan ?: ""}\n" +
                "Catatan: ${pesananData?.catatanPemesan ?: ""}\n" +
                "Ongkir: Rp. ${pesananData?.ongkir ?: 0}\n" +
                "Total Harga: Rp. ${pesananData?.totalHarga ?: 0}\n" +
                "Status : ${pesananData?.statusPembayaran ?: ""}"
        tvDetailPesananAdmin.text = detailPesananUserAdmin
    }
}