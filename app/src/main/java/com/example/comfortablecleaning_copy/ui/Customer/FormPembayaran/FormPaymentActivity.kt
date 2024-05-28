package com.example.comfortablecleaning_copy.Customer.FormPembayaran

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class FormPaymentActivity : AppCompatActivity() {

    private lateinit var tvNamaProdukItemForm: TextView
    private lateinit var tvHargaItemForm: TextView
    private lateinit var btnKurang: ImageButton
    private lateinit var btnTambah: ImageButton
    private lateinit var tvQty: TextView
    private lateinit var edtNama: EditText
    private lateinit var edtNoTelp: EditText
    private lateinit var edtAlamat: EditText
    private lateinit var edtCatatan: EditText
    private lateinit var btnBayarSekarang: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var detailPesanan: TextView
    private var qty: Int = 1
    private var ongkir: Int = 10000

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_payment)

        initializeViews()
        setupListeners()
        updateDetailPesanan()

        val selectedData = intent.getParcelableExtra<Admin>("selectedData")
        tvNamaProdukItemForm.text = selectedData?.namaProduk ?: ""
        tvHargaItemForm.text = "Rp. " + selectedData?.harga ?: "0"

        database = FirebaseDatabase.getInstance().reference.child("pesanan")

        btnBayarSekarang = findViewById(R.id.btn_bayar_sekarang)
        btnBayarSekarang.setOnClickListener {
            saveOrderToDatabase()
        }
    }

    private fun initializeViews() {
        btnKurang = findViewById(R.id.btn_kurang)
        btnTambah = findViewById(R.id.btn_tambah)
        tvQty = findViewById(R.id.tv_qty)
        edtNama = findViewById(R.id.edt_nama_pemesan)
        edtNoTelp = findViewById(R.id.edt_no_telp_pemesan)
        edtAlamat = findViewById(R.id.edt_alamat_pemesan)
        edtCatatan = findViewById(R.id.edt_catatan_pemesan)
        radioGroup = findViewById(R.id.radioGroup)
        detailPesanan = findViewById(R.id.detail_pesanan)
        tvNamaProdukItemForm = findViewById(R.id.tv_nama_produk_item_form)
        tvHargaItemForm = findViewById(R.id.tv_harga_item_form)
    }

    private fun getOngkir(): Int {
        val rbBekTim = findViewById<RadioButton>(R.id.rb_bektim)
        return if (rbBekTim.isChecked) 0 else 10000
    }

    private fun setupListeners() {
        btnKurang.setOnClickListener {
            if (qty > 1) {
                qty--
                tvQty.text = qty.toString()
                updateDetailPesanan()
            }
        }

        btnTambah.setOnClickListener {
            qty++
            tvQty.text = qty.toString()
            updateDetailPesanan()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateDetailPesanan()
            }
        }

        edtNama.addTextChangedListener(textWatcher)
        edtNoTelp.addTextChangedListener(textWatcher)
        edtAlamat.addTextChangedListener(textWatcher)
        edtCatatan.addTextChangedListener(textWatcher)

        radioGroup.setOnCheckedChangeListener { _, _ ->
            updateDetailPesanan()
        }
    }

    private fun updateDetailPesanan() {
        val quantity = qty
        val namaPemesan = edtNama.text.toString()
        val noTelpPemesan = edtNoTelp.text.toString()
        val alamatPemesan = edtAlamat.text.toString()
        val catatanPemesan = edtCatatan.text.toString()
        val selectedData = intent.getParcelableExtra<Admin>("selectedData")

        val hargaPerItem = try {
            selectedData?.harga?.toInt() ?: 0
        } catch (e: NumberFormatException) {
            0
        }

        ongkir = getOngkir()
        val totalHarga = hargaPerItem * quantity + ongkir

        val rbBekTim = findViewById<RadioButton>(R.id.rb_bektim)
        val daerahPemesan = if (rbBekTim.isChecked) "Bekasi Timur" else "Di Luar Daerah Bekasi Timur"

        val detailPesananText = "Nama: $namaPemesan\n" +
                "Pesanan: ${selectedData?.namaProduk}\n" +
                "Qty: $quantity\n" +
                "Nomor Telepon: $noTelpPemesan\n" +
                "Daerah: $daerahPemesan\n" +
                "Alamat: $alamatPemesan\n" +
                "Catatan: $catatanPemesan\n" +
                "Ongkir: Rp. $ongkir\n" +
                "Total Harga: Rp. $totalHarga"
        detailPesanan.text = detailPesananText
    }

    private fun saveOrderToDatabase() {
        val selectedData = intent.getParcelableExtra<Admin>("selectedData")

        val namaPemesan = edtNama.text.toString()
        val noTelpPemesan = edtNoTelp.text.toString()
        val alamatPemesan = edtAlamat.text.toString()
        val catatanPemesan = edtCatatan.text.toString()
        val hargaPerItem = try {
            selectedData?.harga?.toInt() ?: 0
        } catch (e: NumberFormatException) {
            0
        }
        ongkir = getOngkir()
        val totalHarga = hargaPerItem * qty + ongkir

        val rbBekTim = findViewById<RadioButton>(R.id.rb_bektim)
        val daerahPemesan = if (rbBekTim.isChecked) "Bekasi Timur" else "Di Luar Daerah Bekasi Timur"
        val orderId = UUID.randomUUID().toString()

        val pesanan = Pesanan(
            idPesanan = orderId,
            namaPemesan = namaPemesan,
            namaProduk = selectedData?.namaProduk,
            quantity = qty,
            noTelpPemesan = noTelpPemesan,
            daerahPemesan = daerahPemesan,
            alamatPemesan = alamatPemesan,
            catatanPemesan = catatanPemesan,
            ongkir = ongkir,
            totalHarga = totalHarga,
            status = "menunggu" // Set default status
        )

        database.child(orderId).setValue(pesanan).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Pesanan anda berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra("navigateTo", "BerandaFragment") // Pass the fragment name
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Gagal memesan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
