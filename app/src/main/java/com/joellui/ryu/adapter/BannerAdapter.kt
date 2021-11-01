package com.joellui.ryu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.joellui.ryu.R

class BannerAdapter(
    var banner: List<BannerCover>
): RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){ }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_item,parent,false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.itemView.apply {

            var title : TextView = findViewById(R.id.TVtitle)
            var img : ImageView = findViewById(R.id.IVbannerCover)
            var score : TextView = findViewById(R.id.TVscoreBanner)

            title.text = banner[position].title
            score.text = banner[position].score
            img.load(banner[position].img)

            var color = banner[position].cover_color

            if (color != null) {
                score.setTextColor(Color.parseColor(color))
            }

        }
    }

    override fun getItemCount(): Int {
        return banner.size
    }
}