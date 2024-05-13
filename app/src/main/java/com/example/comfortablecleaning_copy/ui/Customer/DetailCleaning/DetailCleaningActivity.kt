package com.example.comfortablecleaning_copy.Customer.DetailCleaning

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.comfortablecleaning_copy.Customer.DetailCleaning.Adaptor.ImageViewPagerAdaptor
import com.example.comfortablecleaning_copy.Customer.FormPembayaran.FormPaymentActivity
import com.example.comfortablecleaning_copy.Customer.ListCleaningShoes.ListCleaningShoesActivity
import com.example.comfortablecleaning_copy.R
import com.google.android.material.tabs.TabLayout

class DetailCleaningActivity : AppCompatActivity() {

    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView
    private lateinit var iv3: ImageView

//    image viewpager
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_cleaning)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton : ImageView = findViewById(R.id.iv_back)
        backButton.setOnClickListener {
            val intent = Intent(this, ListCleaningShoesActivity::class.java)
            startActivity(intent)
            finish()
        }

        //menampilkan gambar image
        viewPager2 = findViewById(R.id.vp_image_cleaning)
        iv1 = findViewById(R.id.iv1)
        iv2 = findViewById(R.id.iv2)
        iv3 = findViewById(R.id.iv3)

        val images = listOf(R.drawable.image_detail_pencucian, R.drawable.image_detail_pencucian, R.drawable.image_detail_pencucian)
        val adapter = ImageViewPagerAdaptor(images)
        viewPager2.adapter = adapter

        //gambar lapangan dan dot animation
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageSelected(position)
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

        val btnPesanCleaning : Button = findViewById(R.id.btn_pesan_cleaning)
        btnPesanCleaning.setOnClickListener {
            val intent = Intent(this, FormPaymentActivity::class.java)
            startActivity(intent)
        }
    }

    //gambar lapangan dan dot animation
    fun changeColor(){
        when(viewPager2.currentItem){
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