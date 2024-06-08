package com.example.comfortablecleaning_copy.Customer.DetailCleaning.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.comfortablecleaning_copy.R
import com.squareup.picasso.Picasso

class ImageViewPagerAdaptor(private val images: List<String>) : RecyclerView.Adapter<ImageViewPagerAdaptor.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curImage = images[position]
        Picasso.get().load(curImage).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
