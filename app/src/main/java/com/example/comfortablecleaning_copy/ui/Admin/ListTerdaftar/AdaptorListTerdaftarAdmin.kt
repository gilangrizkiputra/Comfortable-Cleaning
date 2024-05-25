package com.example.comfortablecleaning_copy.ui.Admin.ListTerdaftar

import android.content.Context
import android.content.Intent
import android.media.Image
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
import com.example.comfortablecleaning_copy.ui.Entitas.Admin
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdaptorListTerdaftarAdmin(private val mlist: List<Admin>, private val context: Context) :
    RecyclerView.Adapter<AdaptorListTerdaftarAdmin.MyViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("admin")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_terdaftar, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mlist[position]
        holder.tvJudulJenis.text = "${item.jenis}"
        holder.tvNamaProduk.text = "${item.namaProduk}"
        holder.tvHargaProduk.text = "Rp. ${item.harga}"

        holder.btnHapus.setOnClickListener {
            item.namaProduk?.let { it1 -> database.child(it1).setValue(null) }
            Toast.makeText(context, "Item berhasil di hapus", Toast.LENGTH_SHORT).show()
        }

        holder.btnEdit.setOnClickListener {
            val item = mlist.getOrNull(position)
            if (item != null) {
                val jenis = item.jenis
                val namaProduk = item.namaProduk
                val harga = item.harga
                val deskripsi = item.deskripsi
                val edit = Intent(context, EditDataCleaningActivity::class.java)
                edit.putExtra("jenis", jenis)
                edit.putExtra("namaProduk", namaProduk)
                edit.putExtra("harga", harga)
                edit.putExtra("deskripsi", deskripsi)
                context.startActivity(edit)
            } else {
                Toast.makeText(context, "Terjadi kesalahan saat mengambil data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudulJenis: TextView = itemView.findViewById(R.id.tv_list_judul_jenis)
        val tvNamaProduk: TextView = itemView.findViewById(R.id.tv_list_nama_produk)
        val tvHargaProduk: TextView = itemView.findViewById(R.id.tv_list_harga_produk)
        val btnHapus: ImageButton = itemView.findViewById(R.id.ib_list_hapus)
        val btnEdit : ImageButton = itemView.findViewById(R.id.ib_list_edit)
        val ivProduk: ImageView = itemView.findViewById(R.id.iv_list_produk)
    }
}
