package com.example.comfortablecleaning_copy.Customer.Profile.BantuanDanLaporan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.comfortablecleaning_copy.R

class BantuanDanLaporanActivity : AppCompatActivity() {

    private lateinit var edtNamaBantuanLaporan: EditText
    private lateinit var edtEmailBantuanLaporan: EditText
    private lateinit var edtNoTelpBantuanLaporan: EditText
    private lateinit var edtLaporan: EditText
    private lateinit var btnLapor: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bantuan_dan_laporan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtNamaBantuanLaporan = findViewById(R.id.edt_nama_bantuan_laporan)
        edtEmailBantuanLaporan = findViewById(R.id.edt_email_bantuan_laporan)
        edtNoTelpBantuanLaporan = findViewById(R.id.edt_no_telp_bantuan_laporan)
        edtLaporan = findViewById(R.id.edt_laporan_bantuan_dan_laporan)
        btnLapor = findViewById(R.id.btn_lapor)

        btnLapor.setOnClickListener {
            val nama = edtNamaBantuanLaporan.text.toString().trim()
            val email = edtEmailBantuanLaporan.text.toString().trim()
            val noTelp = edtNoTelpBantuanLaporan.text.toString().trim()
            val laporan = edtLaporan.text.toString().trim()


            val emailDeveloper = "putra00gilang@gmail.com"
            val subjek = "Laporan User Comfortable Cleaning"
            val isiEmail = "Nama: $nama\nEmail: $email\nNo. Telp: $noTelp\n\nLaporan:\n$laporan"

            // Buka aplikasi email default dengan data yang diinput
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailDeveloper))
                putExtra(Intent.EXTRA_SUBJECT, subjek)
                putExtra(Intent.EXTRA_TEXT, isiEmail)
            }

            try {
                startActivity(Intent.createChooser(intent, "send Email"))
            }catch (e: Exception){
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}