package com.joellui.ryu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joellui.ryu.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.anime_cover_item.view.*

class GridAdapter(
    var covers: List<CoverData>
) : RecyclerView.Adapter<GridAdapter.CoverViewHolder>() {

    inner class CoverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.anime_cover_item, parent, false)
        return CoverViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoverViewHolder, position: Int) {
        holder.itemView.apply {
            TVtitle.text = covers[position].title

            Picasso.with(this.context).load(covers[position].img).placeholder(R.drawable.cover_loader_image).fit().into(IVanimeCover)
        }
    }

    override fun getItemCount(): Int {
        return covers.size
    }
}