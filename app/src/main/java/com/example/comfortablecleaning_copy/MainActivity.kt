package com.example.comfortablecleaning_copy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.comfortablecleaning_copy.Customer.Beranda.BerandaFragment
import com.example.comfortablecleaning_copy.Customer.Pesanan.PesananFragment
import com.example.comfortablecleaning_copy.Customer.Profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigationView = findViewById(R.id.btm_nav_bar)

        bottomNavigationView.setOnItemSelectedListener {menuItem ->
            when(menuItem.itemId){
                R.id.berandaFragment ->{
                    replaceFragment(BerandaFragment())
                    true
                }
                R.id.pesananFragment ->{
                    replaceFragment(PesananFragment())
                    true
                }
                R.id.profileFragment ->{
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(BerandaFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }
}