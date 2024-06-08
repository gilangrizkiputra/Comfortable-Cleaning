package com.example.comfortablecleaning_copy.Customer.FormPembayaran

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.comfortablecleaning_copy.MainActivity
import com.example.comfortablecleaning_copy.PaymentHMidtrans
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONArray
import org.json.JSONObject
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
    private lateinit var auth: FirebaseAuth

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

        auth = FirebaseAuth.getInstance()

        btnBayarSekarang = findViewById(R.id.btn_bayar_sekarang)
        btnBayarSekarang.setOnClickListener {
            if (isFormValid()){
                showCustomDialog()
            }else{
                Toast.makeText(this, "Data belum lengkap", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isFormValid(): Boolean {
        return edtNama.text.isNotEmpty() &&
                edtNoTelp.text.isNotEmpty() &&
                edtAlamat.text.isNotEmpty() &&
                radioGroup.checkedRadioButtonId != -1
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.payment_dialog)

        val buttonCanceled: Button = dialog.findViewById(R.id.btn_batal_payment)
        val buttonAccept: Button = dialog.findViewById(R.id.btn_terima_payment)


        buttonCanceled.setOnClickListener {
            Toast.makeText(this, "Anda membatalkan pesanan", Toast.LENGTH_LONG).show()
            dialog.dismiss() // Tutup dialog jika dibatalkan
        }

        buttonAccept.setOnClickListener {
            Toast.makeText(this, "HARAP TUNGGU! Anda akan di alihkan ke web browser", Toast.LENGTH_LONG).show()
            val currentUser = auth.currentUser
            val email = currentUser?.email ?: "default@example.com"

            val customerDetails = JSONObject().apply {
                put("name", edtNama.text.toString())
                put("email", email)
                put("phone", edtNoTelp.text.toString())
                put("billing_address", JSONObject().apply {
                    put("address", edtAlamat.text.toString())
                })
                put("shipping_address", JSONObject().apply {
                    put("address", edtAlamat.text.toString())
                })
            }

            val itemDetails = JSONArray().apply {
                put(JSONObject().apply {
                    put("id", "ID_PRODUK")
                    put("price", tvHargaItemForm.text.toString().replace("Rp. ", "").toInt())
                    put("quantity", qty)
                    put("name", tvNamaProdukItemForm.text.toString())
                })
            }

            val totalAmount = tvHargaItemForm.text.toString().replace("Rp. ", "").toInt() * qty + ongkir
            PaymentHMidtrans.generatePaymentLink(this@FormPaymentActivity, totalAmount, customerDetails, itemDetails)
            dialog.dismiss() // Tutup dialog jika diterima
        }

        dialog.show()
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

        // Hide detailPesanan TextView initially
        detailPesanan.visibility = View.GONE
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
                if (s?.isNotEmpty() == true) {
                    detailPesanan.visibility = View.VISIBLE
                }
                updateDetailPesanan()
            }
        }

        edtNama.addTextChangedListener(textWatcher)
        edtNoTelp.addTextChangedListener(textWatcher)
        edtAlamat.addTextChangedListener(textWatcher)
        edtCatatan.addTextChangedListener(textWatcher)

        radioGroup.setOnCheckedChangeListener { _, _ ->
            detailPesanan.visibility = View.VISIBLE
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

    //simpan data ke database jika pembayaran sudah selesai
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
        val orderId = UUID.randomUUID().toString().take(8)
        val userId = FirebaseAuth.getInstance().currentUser?.uid // Dapatkan ID pengguna saat ini

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
            status = "menunggu", // Set default status
            jenis = selectedData?.jenis,
            statusPembayaran = "",
            userId = userId // Set userId
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
