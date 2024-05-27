package com.example.comfortablecleaning_copy.ui.Customer.Pesanan.Adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.Admin.EditData.Cleaning.EditDataCleaningActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Admin.ListTerdaftar.AdaptorListTerdaftarAdmin
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdaptorPesanan(private val mlist: List<Pesanan>, private val context: Context) :
    RecyclerView.Adapter<AdaptorPesanan.MyViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("pesanan")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_cleaning, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mlist[position]
        holder.tvJenisItemcPesanan.text = "${item.idPesanan}"
        holder.tvNamaProdukPesanan.text = "${item.namaProduk}"
        holder.tvNamaUserPemesan.text = "Pesanan oleh ${item.namaPemesan}"
        holder.tvQtyPesanan.text = "Qty. ${item.quantity}"
        holder.tvStatusPesananUser.text = "${item.status}"

    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val tvJenisItemcPesanan : TextView = itemView.findViewById(R.id.tv_jenis_itemc_pesanan)
         val tvNamaProdukPesanan : TextView = itemView.findViewById(R.id.tv_nama_produk_pesanan)
         val tvNamaUserPemesan : TextView = itemView.findViewById(R.id.tv_nama_user_pemesan)
         val tvQtyPesanan : TextView = itemView.findViewById(R.id.tv_qty_pesanan)
         val tvStatusPesananUser : TextView = itemView.findViewById(R.id.tv_status_pesanan_user)
    }
}