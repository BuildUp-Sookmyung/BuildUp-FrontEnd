package com.example.buildupfrontend.record

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R

class AddCategoryRecyclerViewAdapter(
    private val dialog: CategoryDialog,
    private var dataList: ArrayList<AddCategoryRecyclerViewData>,
    private var selectedPos:Int
): RecyclerView.Adapter<AddCategoryRecyclerViewAdapter.ViewHolder>() {
    private var selectPos=selectedPos

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView =itemView.findViewById(R.id.iv_add_category)

        fun bind(data: AddCategoryRecyclerViewData){
            image.setImageResource(data.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_category,parent,false)
//        Log.e("AddCategoryRecyclerView","$selectPos")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])

        holder.image.setOnClickListener{
            selectPos=holder.adapterPosition
            notifyDataSetChanged()
            var iconId=dataList[position].iconId

            dialog.savePage(dialog.getCurrentPage())
            dialog.saveIconId(iconId)

            Log.e("Clicked Category Id","${dataList[position].iconId}")
            Log.e("iconId","$iconId")
        }
        holder.image.setColorFilter(if(selectPos==position) Color.parseColor("#845EF1") else Color.parseColor("#575757"))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(newData: ArrayList<AddCategoryRecyclerViewData>, pos:Int) {
        dataList=newData
        selectPos=pos
        Log.e("selectedPos","$selectedPos")
        notifyDataSetChanged()
    }
}