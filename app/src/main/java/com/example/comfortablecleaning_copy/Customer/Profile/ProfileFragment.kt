package com.example.comfortablecleaning_copy.Customer.Profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.comfortablecleaning_copy.Customer.Profile.BantuanDanLaporan.BantuanDanLaporanActivity
import com.example.comfortablecleaning_copy.Customer.Profile.GantiKataSandi.GantiKataSandiActivity
import com.example.comfortablecleaning_copy.Customer.Profile.TentangKami.TentangKamiActivity
import com.example.comfortablecleaning_copy.Login.LoginActivity
import com.example.comfortablecleaning_copy.R

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val ubahFotoProfile : LinearLayout = view.findViewById(R.id.ubah_foto_profil)
        val gantiKataSandi : LinearLayout = view.findViewById(R.id.ganti_kata_sandi)
        val tentangKami : LinearLayout = view.findViewById(R.id.tentang_kami)
        val BantuanDanLaporan : LinearLayout = view.findViewById(R.id.bantuan_dan_laporan)
        val btnKeluar : Button = view.findViewById(R.id.btn_keluar)

        gantiKataSandi.setOnClickListener {
            val intent = Intent(activity, GantiKataSandiActivity::class.java)
            startActivity(intent)
        }

        tentangKami.setOnClickListener {
            val intent = Intent(activity, TentangKamiActivity::class.java)
            startActivity(intent)
        }

        BantuanDanLaporan.setOnClickListener {
            val intent = Intent(activity, BantuanDanLaporanActivity::class.java)
            startActivity(intent)
        }

        btnKeluar.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}