package com.example.buildupfrontend.record

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R

class AddCategoryViewpagerAdapter (
    private val context: Context,
    private var dataList: ArrayList<AddCategoryRecyclerViewData>
): RecyclerView.Adapter<AddCategoryViewpagerAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(itemView: ViewGroup): RecyclerView.ViewHolder(itemView){
        val recyclerView=itemView.findViewById<RecyclerView>(R.id.recyclerview_add_category)

        fun bind(data: AddCategoryRecyclerViewData){
            recyclerView.layoutManager=
                GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
            recyclerView.adapter=AddCategoryRecyclerViewAdapter(context, dataList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager_category,parent,false)
        return PagerViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = 3
}