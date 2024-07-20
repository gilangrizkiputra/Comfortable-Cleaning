package com.example.comfortablecleaning_copy.Onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.Register.RegisterActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingActivity : AppCompatActivity() {

//    skip onboarding ketika sudah menginstall
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //mengecek apakah aplikasi sudah pernah di install atau tidak jika sudah maka onboarding di hilangkan
        sharedPreferences = getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (isFirstRun){
            showOnboarding()
            with(sharedPreferences.edit()){
                putBoolean("isFirstRun", false)
                apply()
            }
        }else{
            navigateToLogin()
        }
    }

    private fun showOnboarding() {
        val viewPager = findViewById<ViewPager2>(R.id.viewPager2)
        val adapter = OnboardingViewPagerAdapter(this)
        viewPager.adapter = adapter

        val dotsIndicator: DotsIndicator = findViewById(R.id.dots_indicator)

        //Menghubungkan DotsIndicator dengan ViewPager2
        dotsIndicator.setViewPager2(viewPager)


        //button mulai next fragment
        val btnMulai : Button = findViewById(R.id.btn_mulai)
        btnMulai.setOnClickListener {

            val nextIndex = viewPager.currentItem + 1

            if (nextIndex < adapter.itemCount) {
                viewPager.setCurrentItem(nextIndex, true)
            } else {
                navigateToRegister()
            }
        }
        //intent ke login
        val tvLogin : TextView = findViewById(R.id.tv_login)
        tvLogin.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

