package com.example.comfortablecleaning_copy.ui.Customer.ListCleaningShoes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.R
import com.example.comfortablecleaning_copy.ui.Entitas.Produk
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdaptorListCleaningShoes(private val mlist: List<Produk>, private val context: Context) :
    RecyclerView.Adapter<AdaptorListCleaningShoes.MyViewHolder>() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("produk")

    private var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cleaning_shoes, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = mlist[position]
        holder.tvJudulCleaning.text = "${item.jenis}"
        holder.tvNamaProdukCleaning.text = "${item.namaProduk}"
        holder.tvHargaProdukCleaning.text = "Rp. ${item.harga}"
        holder.tvEstimasiCleaning.text = "${item.estimasi}"
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvJudulCleaning: TextView = itemView.findViewById(R.id.tv_list_judul_cleaning)
        val tvNamaProdukCleaning: TextView = itemView.findViewById(R.id.tv_list_nama_produk_cleaning)
        val tvHargaProdukCleaning: TextView = itemView.findViewById(R.id.tv_list_harga_cleaning)
        val tvEstimasiCleaning: TextView = itemView.findViewById(R.id.tv_estimasi_cleaning)
        val ivProduk: ImageView = itemView.findViewById(R.id.iv_list_cleaning)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            itemClickListener?.onItemClick(adapterPosition)
        }
    }
}