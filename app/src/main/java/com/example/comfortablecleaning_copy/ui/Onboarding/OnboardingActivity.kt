package com.example.comfortablecleaning_copy.Onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.Onboarding.Screen.OnboardingScreen1
import com.example.comfortablecleaning_copy.Onboarding.Screen.OnboardingScreen2
import com.example.comfortablecleaning_copy.Onboarding.Screen.OnboardingScreen3
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.Register.RegisterActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        //intent ke login
        val tvLogin : TextView = findViewById(R.id.tv_login)
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}