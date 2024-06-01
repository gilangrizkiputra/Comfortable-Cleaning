package com.example.comfortablecleaning_copy.Customer.DetailCleaning.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.comfortablecleaning_copy.R
import androidx.recyclerview.widget.RecyclerView

class ImageViewPagerAdaptor(private val images:List<Int>): RecyclerView.Adapter<ImageViewPagerAdaptor.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val  imageView: ImageView = itemView.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewPagerAdaptor.ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewPagerAdaptor.ViewPagerViewHolder, position: Int) {
        val curImage = images[position]
        holder.imageView.setImageResource(curImage)
    }


    override fun getItemCount(): Int {
        return images.size
    }

}