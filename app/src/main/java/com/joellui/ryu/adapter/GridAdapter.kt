package com.joellui.ryu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.joellui.ryu.R
import kotlinx.android.synthetic.main.anime_cover_item.view.*

class GridAdapter(
    var covers: List<CoverData>,
    val listener: OnClickListener
) : RecyclerView.Adapter<GridAdapter.CoverViewHolder>() {

    inner class CoverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener
    {
        init {

            itemView.setOnClickListener(
                this
            )
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.anime_cover_item, parent, false)
        return CoverViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoverViewHolder, position: Int) {
        holder.itemView.apply {
            TVtitle.text = covers[position].title

            IVanimeCover.load(covers[position].img){
                crossfade(true)
                crossfade(1000)
                size(500,750)
                transformations(RoundedCornersTransformation(30f))
            }

        }
    }

    override fun getItemCount(): Int {
        return covers.size
    }

    interface OnClickListener{
        fun onClick(position: Int)
    }

}