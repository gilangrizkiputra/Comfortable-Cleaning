package com.example.comfortablecleaning_copy.Customer.FormPembayaran

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import org.w3c.dom.Text

class FormPaymentActivity : AppCompatActivity() {

    private lateinit var tvNamaProdukItemForm: TextView
    private lateinit var tvHargaItemForm: TextView
    private lateinit var btnKurang : ImageButton
    private lateinit var btnTambah : ImageButton
    private lateinit var tvQty : TextView
    private lateinit var edtNama: EditText
    private lateinit var edtNoTelp: EditText
    private lateinit var edtAlamat: EditText
    private lateinit var edtCatatan: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var detailPesanan: TextView
    private var qty: Int = 1
    private var ongkir: Int = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_payment)

        btnKurang = findViewById(R.id.btn_kurang)
        btnTambah = findViewById(R.id.btn_tambah)
        tvQty = findViewById(R.id.tv_qty)

        tvQty.text = qty.toString()

        btnKurang.setOnClickListener {
            if (qty > 1) {
                qty--
                tvQty.text = qty.toString()
            }
        }

        btnTambah.setOnClickListener {
            qty++
            tvQty.text = qty.toString()
        }

        edtNama = findViewById(R.id.edt_nama_pemesan)
        edtNoTelp = findViewById(R.id.edt_no_telp_pemesan)
        edtAlamat = findViewById(R.id.edt_alamat_pemesan)
        edtCatatan = findViewById(R.id.edt_catatan_pemesan)
        radioGroup = findViewById(R.id.radioGroup)
        detailPesanan = findViewById(R.id.detail_pesanan)

        // Buat TextWatcher untuk setiap EditText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateDetailPesanan()
            }
        }
        // Pasang TextWatcher ke setiap EditText
        edtNama.addTextChangedListener(textWatcher)
        edtNoTelp.addTextChangedListener(textWatcher)
        edtAlamat.addTextChangedListener(textWatcher)
        edtCatatan.addTextChangedListener(textWatcher)
        tvQty.addTextChangedListener(textWatcher)
        // Pasang listener untuk RadioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            updateDetailPesanan()
        }

        //ambil data namaProduk dan harga
        val selectedData = intent.getParcelableExtra<Admin>("selectedData")
        tvNamaProdukItemForm = findViewById(R.id.tv_nama_produk_item_form)
        tvHargaItemForm = findViewById(R.id.tv_harga_item_form)
        if (selectedData != null) {
            tvNamaProdukItemForm.text = selectedData.namaProduk
            tvHargaItemForm.text = "Rp. ${selectedData.harga}"
        }
    }

    private fun updateDetailPesanan() {
        val quantity = tvQty.text.toString()
        val namaPemesan = edtNama.text.toString()
        val noTelpPemesan = edtNoTelp.text.toString()
        val alamatPemesan = edtAlamat.text.toString()
        val catatanPemesan = edtCatatan.text.toString()
        val selectedData = intent.getParcelableExtra<Admin>("selectedData")

        val rbBekTim = findViewById<RadioButton>(R.id.rb_bektim)
        val daerahPemesan = if (rbBekTim.isChecked) "Bekasi Timur" else "Di Luar Daerah Bekasi Timur"

        val detailPesananText = "Nama: $namaPemesan\n" +
                "Pesanan : ${selectedData?.namaProduk}\n" +
                "Qty : $quantity\n" +
                "Nomor Telepon: $noTelpPemesan\n" +
                "Daerah: $daerahPemesan\n" +
                "Alamat: $alamatPemesan\n" +
                "Catatan: $catatanPemesan" +
                "Ongkir: $catatanPemesan" +
                "Total Harga: $catatanPemesan"
        detailPesanan.text = detailPesananText
    }
}
