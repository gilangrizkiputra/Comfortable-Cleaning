package com.example.comfortablecleaning_copy.Admin.Pesanan

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.comfortablecleaning_copy.Customer.Pesanan.Adaptor.FragmentPageAdaptor
import com.example.comfortablecleaning_copy.R
import com.google.android.material.tabs.TabLayout

class PesananActivity : AppCompatActivity() {

    private lateinit var vpPesananSelesaiAdmin: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: FragmentPageAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pesanan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tabLayout = findViewById(R.id.tablayout_pesanan_admin)
        vpPesananSelesaiAdmin = findViewById(R.id.vp_pesananAdmin)
        adapter = FragmentPageAdaptor(supportFragmentManager, lifecycle)
        vpPesananSelesaiAdmin.adapter = adapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null){
                    vpPesananSelesaiAdmin.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        vpPesananSelesaiAdmin.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
    }
}