package com.example.comfortablecleaning_copy.Customer.Pesanan.DetailPesanan

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan

class DetailPesananActivity : AppCompatActivity() {

    private lateinit var tvDetailPesanan: TextView
    private lateinit var ivBackBeranda : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_pesanan)

        ivBackBeranda = findViewById(R.id.iv_back_beranda_detail_pesanan)
        ivBackBeranda.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("navigateTo", "PesananFragment") // Pass the fragment name
            }
            startActivity(intent)
            finish()
        }

        val pesananData = intent.getParcelableExtra<Pesanan>("PESANAN_DATA")
        tvDetailPesanan = findViewById(R.id.tv_detail_pesanan)

        val detailPesananUser = "Nama: ${pesananData?.namaPemesan ?: ""}\n" +
                "Pesanan: ${pesananData?.namaProduk ?: ""}\n" +
                "Qty: ${pesananData?.quantity ?: 0}\n" +
                "Nomor Telepon: ${pesananData?.noTelpPemesan ?: ""}\n" +
                "Daerah: ${pesananData?.daerahPemesan ?: ""}\n" +
                "Alamat: ${pesananData?.alamatPemesan ?: ""}\n" +
                "Catatan: ${pesananData?.catatanPemesan ?: ""}\n" +
                "Ongkir: Rp. ${pesananData?.ongkir ?: 0}\n" +
                "Total Harga: Rp. ${pesananData?.totalHarga ?: 0}\n" +
                "Status : ${pesananData?.statusPembayaran ?: ""}"
        tvDetailPesanan.text = detailPesananUser
    }
}