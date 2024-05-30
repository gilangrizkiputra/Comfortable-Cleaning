package com.example.comfortablecleaning_copy.Customer.Pesanan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.comfortablecleaning_copy.Customer.DetailCleaning.DetailCleaningActivity
import com.example.comfortablecleaning_copy.Customer.Pesanan.DetailPesanan.DetailPesananActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Customer.Pesanan.Adaptor.AdaptorPesanan
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class PesananFragment : Fragment() {

    lateinit var rvPesananUser : RecyclerView
    lateinit var adaptorPesanan: AdaptorPesanan
    private lateinit var database: DatabaseReference
    private var arrayList: ArrayList<Pesanan> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pesanan, container, false)


        rvPesananUser = view.findViewById(R.id.rv_pesanan_user)

        database = FirebaseDatabase.getInstance().getReference("pesanan")

        rvPesananUser.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }

        adaptorPesanan = AdaptorPesanan(arrayList, requireContext())
        adaptorPesanan.setOnItemClickListener(object : AdaptorPesanan.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val selectedData = arrayList[position]
                val intent = Intent(requireContext(), DetailPesananActivity::class.java)
                intent.putExtra("PESANAN_DATA", selectedData)
                startActivity(intent)
            }
        })

        showData()

        return view
    }

    private fun showData() {
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                arrayList.clear()
//                for (item in snapshot.children){
//                    val pesanan = Pesanan()
//                    pesanan.idPesanan = item.child("idPesanan").getValue(String::class.java)
//                    pesanan.namaPemesan = item.child("namaPemesan").getValue(String::class.java)
//                    pesanan.namaProduk = item.child("namaProduk").getValue(String::class.java)
//                    pesanan.quantity = item.child("quantity").getValue(Int::class.java)
//                    pesanan.noTelpPemesan = item.child("noTelpPemesan").getValue(String::class.java)
//                    pesanan.daerahPemesan = item.child("daerahPemesan").getValue(String::class.java)
//                    pesanan.alamatPemesan = item.child("alamatPemesan").getValue(String::class.java)
//                    pesanan.catatanPemesan = item.child("catatanPemesan").getValue(String::class.java)
//                    pesanan.ongkir = item.child("ongkir").getValue(Int::class.java)
//                    pesanan.totalHarga = item.child("totalHarga").getValue(Int::class.java)
//                    pesanan.status = item.child("status").getValue(String::class.java) ?: "menunggu"
//                }
                for (item in snapshot.children){
                    val pesanan = item.getValue(Pesanan::class.java)
                    pesanan?.let { arrayList.add(it) }
                }
                adaptorPesanan = AdaptorPesanan(arrayList, requireContext())
                rvPesananUser.adapter = adaptorPesanan
                adaptorPesanan.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}