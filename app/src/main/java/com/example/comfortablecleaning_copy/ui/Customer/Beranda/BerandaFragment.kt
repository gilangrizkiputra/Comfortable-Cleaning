package com.example.comfortablecleaning_copy.Customer.Beranda

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.comfortablecleaning_copy.Customer.ListCleaningShoes.ListCleaningShoesActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.databinding.FragmentBerandaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BerandaFragment : Fragment() {

    private lateinit var binding: FragmentBerandaBinding

    private lateinit var tvUsernameBeranda: TextView
    private lateinit var ivGambarProfilBeranda : ImageView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBerandaBinding.inflate(inflater, container, false)
        val view = binding.root

        // button to layout activity
        val cleaningKategori = view.findViewById<RelativeLayout>(R.id.cleaning_kategori)
        cleaningKategori.setOnClickListener {
            val intent = Intent(activity, ListCleaningShoesActivity::class.java)
            startActivity(intent)
        }

        tvUsernameBeranda = view.findViewById(R.id.tv_username_beranda)
        ivGambarProfilBeranda = view.findViewById(R.id.iv_gambar_profile_beranda)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: ""
        database = FirebaseDatabase.getInstance().getReference("users/$userId")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val username = snapshot.child("username").value.toString()
                    tvUsernameBeranda.text = "Halo, $username"

                    // Mendapatkan profileImageUrl dari Realtime Database
                    val profileImageUrl = snapshot.child("profileImageUrl").value.toString()

                    // Menampilkan gambar profil menggunakan Glide atau library lain
                    if (profileImageUrl.isNotEmpty()) {
                        Glide.with(this@BerandaFragment)
                            .load(profileImageUrl)
                            .into(ivGambarProfilBeranda)
                    } else {
                        // Tampilkan gambar default jika tidak ada gambar profil
                        ivGambarProfilBeranda.setImageResource(R.drawable.image_profil)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.image_content1_home, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.image_content2_home, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                // Tidak digunakan
            }

            override fun onItemSelected(position: Int) {
                if (position == 1) {
                    val ownerPhoneNumber = "+6285810393460"
                    openWhatsAppChat(ownerPhoneNumber)
                } else {
                    val itemPosition = imageList[position]
                    val itemMessage = "Selected image $position"
                    Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun openWhatsAppChat(phoneNumber: String) {
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}