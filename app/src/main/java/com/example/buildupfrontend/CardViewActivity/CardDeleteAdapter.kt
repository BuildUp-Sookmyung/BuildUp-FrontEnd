package com.example.buildupfrontend.CardViewActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R

class CardDeleteAdapter(
    activity: Context,
    private val labels: Array<String>,
    private val blocks: IntArray,
    private val titles: Array<String>,
    private val dates: Array<String>
) : RecyclerView.Adapter<CardDeleteAdapter.MyViewHolder>() {

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var itemlabel: TextView = itemview.findViewById(R.id.tv_label)
        var itemblock: ImageView = itemview.findViewById(R.id.iv_block)
        var itemtitle: TextView = itemview.findViewById(R.id.tv_title)
        var itemdate: TextView = itemview.findViewById(R.id.tv_date)
        var itembox: ConstraintLayout = itemview.findViewById(R.id.cl_checkbox)
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(viewgroup.context).inflate(R.layout.card_delete, viewgroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemlabel.text = labels[position]
        holder.itemblock.setImageResource(blocks[position])
        holder.itemtitle.text = titles[position]
        holder.itemdate.text = dates[position]
    }

    override fun getItemCount(): Int {
        return labels.size
    }

}