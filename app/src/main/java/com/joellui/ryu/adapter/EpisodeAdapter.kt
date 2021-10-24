package com.joellui.ryu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.joellui.ryu.R
import com.joellui.ryu.model.EpisodeDocument


class EpisodeAdapter(
    var episode: List<EpisodeDocument>
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {
    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ep_btn_item, parent, false)
        return EpisodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {

        holder.itemView.apply {
            val epBtn: Button = findViewById(R.id.epButton)
            epBtn.text = episode[position].number.toString()
        }
    }

    override fun getItemCount(): Int {
        return episode.size
    }
}