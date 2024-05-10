package com.example.comfortablecleaning_copy.Customer.Beranda

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.comfortablecleaning_copy.Customer.ListCleaningShoes.ListCleaningShoesActivity
import com.example.comfortablecleaning_copy.R

class BerandaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_beranda, container, false)

        // button to layout activity
        val cleaningKategori = view.findViewById<RelativeLayout>(R.id.cleaning_kategori)
        cleaningKategori.setOnClickListener {
            val intent = Intent(activity, ListCleaningShoesActivity::class.java)
            startActivity(intent)
        }


        return view
    }
}