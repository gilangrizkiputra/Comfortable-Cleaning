package com.example.comfortablecleaning_copy.Customer.Pesanan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.comfortablecleaning_copy.Customer.Pesanan.Adaptor.FragmentPageAdaptor
import com.example.comfortablecleaning_copy.R
import com.google.android.material.tabs.TabLayout

class PesananFragment : Fragment() {

    private lateinit var vpPesananRiwayat: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: FragmentPageAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pesanan, container, false)

        tabLayout = view.findViewById(R.id.tablayout)
        vpPesananRiwayat = view.findViewById(R.id.vp_pesanan)
        adapter = FragmentPageAdaptor(childFragmentManager, lifecycle)
        vpPesananRiwayat.adapter = adapter

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null){
                    vpPesananRiwayat.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        vpPesananRiwayat.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        return view


    }
}