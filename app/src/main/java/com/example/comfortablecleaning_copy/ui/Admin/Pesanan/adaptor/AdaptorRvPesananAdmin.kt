package com.example.comfortablecleaning_copy.ui.Admin.Pesanan.adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.Admin.DetailPesananAdmin.DetailPesananAdminActivity
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Pesanan
import com.google.firebase.database.FirebaseDatabase

class AdaptorRvPesananAdmin(private val mlist: List<Pesanan>, private val context: Context) :
    RecyclerView.Adapter<AdaptorRvPesananAdmin.MyViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_admin, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mlist[position]
        holder.tvJenisItemPesananAdmin.text = item.jenis
        holder.tvNamaProdukPesananAdmin.text = item.namaProduk
        holder.tvNamaUserPemesanAdmin.text = "Pesanan oleh ${item.namaPemesan}"
        holder.tvQtyPesananAdmin.text = "Qty. ${item.quantity}"

        // Setup Dropdown Menu
        val itemStatus = arrayOf("Menunggu", "Terkonfirmasi", "Menjemput", "Proses", "Antar", "Selesai", "Ditolak")
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, itemStatus)
        holder.actUpdateStatus.setAdapter(adapter)

        // Set current status
        holder.actUpdateStatus.setText(item.status, false)

        // Handle status change
        holder.actUpdateStatus.setOnItemClickListener { _, _, position, _ ->
            val selectedStatus = itemStatus[position]
            // Update the status in your data source
            updateOrderStatus(item.idPesanan ?: "", selectedStatus)
        }
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvJenisItemPesananAdmin: TextView = itemView.findViewById(R.id.tv_jenis_item_pesanan_admin)
        val tvNamaProdukPesananAdmin: TextView = itemView.findViewById(R.id.tv_nama_produk_pesanan_admin)
        val tvNamaUserPemesanAdmin: TextView = itemView.findViewById(R.id.tv_nama_user_pemesan_admin)
        val tvQtyPesananAdmin: TextView = itemView.findViewById(R.id.tv_qty_pesanan_admin)
        val actUpdateStatus: AutoCompleteTextView = itemView.findViewById(R.id.act_update_status)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener?.onItemClick(adapterPosition)
            val pesananData = mlist[adapterPosition]
            val intent = Intent(context, DetailPesananAdminActivity::class.java)
            intent.putExtra("PESANAN_DATA", pesananData)
            context.startActivity(intent)
        }
    }

    private fun updateOrderStatus(orderId: String, newStatus: String) {
        val database = FirebaseDatabase.getInstance().getReference("pesanan").child(orderId)
        val updates = mapOf("status" to newStatus)
        database.updateChildren(updates).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Status berhasil diperbarui", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Status gagal diperbarui", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
