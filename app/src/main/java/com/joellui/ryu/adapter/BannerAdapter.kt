package com.joellui.ryu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.joellui.ryu.MainFragment
import com.joellui.ryu.R

class BannerAdapter(
    var banner: List<BannerCover>,
    val listener: OnClickListener
): RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        init {
            itemView.setOnClickListener(
                this
            )
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_item,parent,false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.itemView.apply {

            val title : TextView = findViewById(R.id.TVtitle)
            val img : ImageView = findViewById(R.id.IVbannerCover)
            val score : TextView = findViewById(R.id.TVscoreBanner)

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

    interface OnClickListener {
        fun onClick(position: Int)
    }
}