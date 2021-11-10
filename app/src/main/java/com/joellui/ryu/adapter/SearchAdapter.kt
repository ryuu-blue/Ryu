package com.joellui.ryu.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.joellui.ryu.R
import com.joellui.ryu.model.AnimeData

class SearchAdapter(
    var search_result: List<AnimeData>,
    val listener: OnClickListener
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        init {
            itemView.setOnClickListener(
                this
            )
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position!= RecyclerView.NO_POSITION){
                listener.searchResultClick(position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_row_item,parent,false)
        return  SearchViewHolder(view)
    }

    var statusList: List<String> = listOf("FINISHED", "RELEASING", "NOT_YET_RELEASED", "CANCELLED")
    var formatList: List<String> = listOf("TV", "TV_SHORT", "MOVIE", "SPECIAL", "OVA", "ONA", "MUSIC")

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.itemView.apply {
            val cover = findViewById<ImageView>(R.id.SearchAnimeCover)
            val title = findViewById<TextView>(R.id.SearchTitle)
            val subtitle = findViewById<TextView>(R.id.SearchSubtitle)

            title.text = search_result[position].titles.en
            val sub = "${formatList[search_result[position].format]} || ${statusList[search_result[position].status]}"
            subtitle.text = sub

            val color = search_result[position].cover_color
            if (color != null){
                subtitle.setTextColor(Color.parseColor(color))
            }

            cover.load(search_result[position].cover_image){
                crossfade(true)
                transformations(RoundedCornersTransformation(10f))
            }
        }
    }

    override fun getItemCount(): Int {
        return search_result.size
    }

    interface OnClickListener{
        fun searchResultClick(position: Int)
    }

}