package com.example.comfortablecleaning_copy.Onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.comfortablecleaning_copy.Onboarding.Screen.OnboardingScreen1
import com.example.comfortablecleaning_copy.Onboarding.Screen.OnboardingScreen2
import com.example.comfortablecleaning_copy.Onboarding.Screen.OnboardingScreen3

class OnboardingViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        OnboardingScreen1(),
        OnboardingScreen2(),
        OnboardingScreen3()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}