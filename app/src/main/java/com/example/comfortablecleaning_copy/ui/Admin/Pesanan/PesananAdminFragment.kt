package com.example.comfortablecleaning_copy.Admin.Pesanan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.comfortablecleaning_copy.Admin.DetailPesananAdmin.DetailPesananAdminActivity
import com.example.comfortablecleaning_copy.Customer.Profile.GantiKataSandi.GantiKataSandiActivity
import com.example.comfortablecleaning_copy.R

class PesananAdminFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pesanan_admin, container, false)

        val detailPesanan: LinearLayout = view.findViewById(R.id.item_pesanan)

        detailPesanan.setOnClickListener {
            val intent = Intent(activity, DetailPesananAdminActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}