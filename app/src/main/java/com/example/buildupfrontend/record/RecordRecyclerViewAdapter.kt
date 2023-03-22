package com.example.buildupfrontend.record

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R

class RecordRecyclerViewAdapter(
    private val context: Context,
    private var dataList: ArrayList<RecordRecyclerViewData>,
    ): RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder>() {
    private var selectPos=0

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView =itemView.findViewById(R.id.image_category)
        var category: TextView=itemView.findViewById(R.id.tv_category)
        var layout: LinearLayout=itemView.findViewById(R.id.linear_category)

        fun bind(data: RecordRecyclerViewData){
            image.setImageResource(data.image)
            category.text=data.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])

        if(selectPos==position){
            holder.image.setColorFilter(Color.parseColor("#845EF1"))
            holder.category.setTextColor(Color.parseColor("#845EF1"))
        }
        else{
            holder.image.setColorFilter(Color.parseColor("#575757"))
            holder.category.setTextColor(Color.parseColor("#575757"))
        }

        holder.layout.setOnClickListener{
            var beforePos=selectPos
            selectPos=holder.adapterPosition

            notifyItemChanged(beforePos)
            notifyItemChanged(selectPos)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}