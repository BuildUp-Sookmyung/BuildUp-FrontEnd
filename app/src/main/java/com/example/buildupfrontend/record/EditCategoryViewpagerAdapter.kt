package com.example.buildupfrontend.record

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R

class EditCategoryViewpagerAdapter(
    private val context: Context,
    private val dialog: CategoryEditDialog,
    private var dataList: ArrayList<AddCategoryRecyclerViewData>,
    private var selectedIcon: Int
): RecyclerView.Adapter<EditCategoryViewpagerAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(itemView: ViewGroup): RecyclerView.ViewHolder(itemView){
        val recyclerView=itemView.findViewById<RecyclerView>(R.id.recyclerview_add_category)

        fun bind(data: AddCategoryRecyclerViewData){
            recyclerView.layoutManager=
                GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
            recyclerView.adapter=EditCategoryDialogRecyclerViewAdapter(dialog, dataList, selectedIcon)
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

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}