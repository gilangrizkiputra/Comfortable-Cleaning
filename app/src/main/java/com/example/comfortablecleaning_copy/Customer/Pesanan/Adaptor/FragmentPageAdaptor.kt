package com.example.comfortablecleaning_copy.Customer.Pesanan.Adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.comfortablecleaning_copy.Customer.Pesanan.ListPesanan.ListPesananFragment
import com.example.comfortablecleaning_copy.Customer.Pesanan.Riwayat.ListRiwayatFragment

class FragmentPageAdaptor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ListPesananFragment()
        }else {
            ListRiwayatFragment()
        }
    }
}