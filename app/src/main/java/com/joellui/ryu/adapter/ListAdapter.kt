package com.joellui.ryu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.joellui.ryu.R
import kotlinx.android.synthetic.main.list_item.view.*

class ListAdapter(
    var items: List<CoverData>,
    val listener: OnClickListener
): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
            init {
                itemView.setOnClickListener(
                    this
                )
            }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onClickItemList(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.apply {
            TVtitle.text = items[position].title

            IVanimeCover.load(items[position].img) {
                crossfade(true)
                crossfade(1000)
                size(320, 450)
                transformations(RoundedCornersTransformation(15f))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnClickListener{
        fun onClickItemList(position: Int)
    }
}