package com.example.comfortablecleaning_copy.ui.Admin.Pesanan.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.R

class AdaptorRvPesananAdmin(private val dataList: List<String>) :
    RecyclerView.Adapter<AdaptorRvPesananAdmin.PesananAdminViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    inner class PesananAdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananAdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pesanan_admin, parent, false)
        return PesananAdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: PesananAdminViewHolder, position: Int) {
        val currentData = dataList[position]
        // Tambahkan logika untuk mengikat data ke tampilan
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}