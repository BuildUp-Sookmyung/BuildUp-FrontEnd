package com.example.buildupfrontend.record

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R

class EditCategoryDialogRecyclerViewAdapter(
    private val dialog: CategoryEditDialog,
    private var dataList: ArrayList<AddCategoryRecyclerViewData>,
    private var selectedIcon: Int
): RecyclerView.Adapter<EditCategoryDialogRecyclerViewAdapter.ViewHolder>() {
    private var selectPos=-1

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView =itemView.findViewById(R.id.iv_add_category)

        fun bind(data: AddCategoryRecyclerViewData){
            image.setImageResource(data.image)
            image.setColorFilter(Color.parseColor(data.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_category,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])

        if(selectedIcon!=-1){
            dialog.savePage(dialog.getCurrentPage())
            dialog.savePosition(selectedIcon)
            selectPos=selectedIcon
            selectedIcon=-1
//            holder.image.setColorFilter(if(selectPos==position) Color.parseColor("#845EF1") else Color.parseColor("#575757"))
        }

        holder.image.setOnClickListener{
            selectPos=holder.adapterPosition
            notifyDataSetChanged()

            dialog.savePage(dialog.getCurrentPage())
            dialog.savePosition(position)

            Log.e("Clicked Category Id","${dataList[position].iconId}")
        }
        holder.image.setColorFilter(if(selectPos==position) Color.parseColor("#845EF1") else Color.parseColor("#575757"))
//        dataList[position].color=if(selectPos==position) "#845EF1" else "#575757"
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun changeIconColor(pos: Int){
        dataList[pos].color="#845EF1"
        notifyItemChanged(pos)
        Log.e("changeIconColor","${dataList[pos].color}")
    }

}