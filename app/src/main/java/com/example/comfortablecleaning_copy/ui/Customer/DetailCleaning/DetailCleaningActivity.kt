package com.example.comfortablecleaning_copy.Customer.DetailCleaning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.comfortablecleaning_copy.Customer.DetailCleaning.Adaptor.ImageViewPagerAdaptor
import com.example.comfortablecleaning_copy.Customer.FormPembayaran.FormPaymentActivity
import com.example.comfortablecleaning_copy.Customer.ListCleaningShoes.ListCleaningShoesActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class DetailCleaningActivity : AppCompatActivity() {

    private lateinit var tvHargaItem: TextView
    private lateinit var tvEstimasiDetail: TextView
    private lateinit var tvNamaProdukItem: TextView
    private lateinit var tvDeskripsiDetail: TextView
    private lateinit var viewPager2: ViewPager2
    private lateinit var database: DatabaseReference
    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView
    private lateinit var iv3: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_cleaning)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton: ImageView = findViewById(R.id.iv_back)
        backButton.setOnClickListener {
            val intent = Intent(this, ListCleaningShoesActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvHargaItem = findViewById(R.id.tv_harga_item)
        tvEstimasiDetail = findViewById(R.id.tv_estimasi_detail)
        tvNamaProdukItem = findViewById(R.id.tv_nama_produk_item)
        tvDeskripsiDetail = findViewById(R.id.tv_deskripsi_detail)

        viewPager2 = findViewById(R.id.vp_image_cleaning)
        iv1 = findViewById(R.id.iv1)
        iv2 = findViewById(R.id.iv2)
        iv3 = findViewById(R.id.iv3)

        database = FirebaseDatabase.getInstance().getReference("admin")

        val selectedData = intent.getParcelableExtra<Admin>("selectedData")
        if (selectedData != null) {
            tvHargaItem.text = "Rp ${selectedData.harga}"
            tvEstimasiDetail.text = "Estimasi ${selectedData.estimasi}"
            tvNamaProdukItem.text = selectedData.namaProduk
            tvDeskripsiDetail.text = selectedData.deskripsi

            loadImages(selectedData.idProduk!!)
        }

        val btnPesanCleaning: Button = findViewById(R.id.btn_pesan)
        btnPesanCleaning.setOnClickListener {
            val intent = Intent(this, FormPaymentActivity::class.java)
            intent.putExtra("selectedData", selectedData)
            intent.putExtra("namaProduk", selectedData?.namaProduk)
            intent.putExtra("hargaProduk", selectedData?.harga)
            startActivity(intent)
        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                changeColor()
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                changeColor()
            }
        })
    }

    private fun loadImages(productId: String) {
        database.child(productId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cleaningItem = dataSnapshot.getValue(Admin::class.java)
                cleaningItem?.let {
                    val images = it.imageUrl?.split(",")?.map { url -> url.trim() } ?: listOf()
                    val adapter = ImageViewPagerAdaptor(images)
                    viewPager2.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun changeColor() {
        when (viewPager2.currentItem) {
            0 -> {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.primaryBlue))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
            }
            1 -> {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.primaryBlue))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
            }
            2 -> {
                iv1.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv2.setBackgroundColor(applicationContext.resources.getColor(R.color.grey))
                iv3.setBackgroundColor(applicationContext.resources.getColor(R.color.primaryBlue))
            }
        }
    }
}
