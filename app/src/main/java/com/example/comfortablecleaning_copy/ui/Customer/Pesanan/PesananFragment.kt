package com.example.comfortablecleaning_copy.Customer.Pesanan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.Customer.Pesanan.DetailPesanan.DetailPesananActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Customer.Pesanan.Adaptor.AdaptorPesanan
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PesananFragment : Fragment() {

    lateinit var rvPesananUser : RecyclerView
    lateinit var adaptorPesanan: AdaptorPesanan
    lateinit var tvNoOrder: TextView
    private lateinit var database: DatabaseReference
    private var arrayList: ArrayList<Pesanan> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pesanan, container, false)


        rvPesananUser = view.findViewById(R.id.rv_pesanan_user)
        tvNoOrder = view.findViewById(R.id.tv_no_orders)

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
        val userId = FirebaseAuth.getInstance().currentUser?.uid // Dapatkan ID pengguna saat ini
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return

                arrayList.clear()
                for (item in snapshot.children) {
                    val pesanan = item.getValue(Pesanan::class.java)
                    if (pesanan?.userId == userId) { // Filter berdasarkan userId
                        pesanan?.let { arrayList.add(it) }
                    }
                }
                adaptorPesanan = AdaptorPesanan(arrayList, requireContext())
                rvPesananUser.adapter = adaptorPesanan
                adaptorPesanan.notifyDataSetChanged()


                tvNoOrder.visibility = if (arrayList.isEmpty()) View.VISIBLE else View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                if (!isAdded) return
                Toast.makeText(context, "Terjadi kesalahan: " + error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

}