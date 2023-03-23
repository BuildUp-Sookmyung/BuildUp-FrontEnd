package com.example.buildupfrontend.record

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R
import com.google.android.material.checkbox.MaterialCheckBox

class RecordListRecyclerViewAdapter(
    private var context: Context,
    private val titles: Array<String>,
    private val dates: Array<String>
) : RecyclerView.Adapter<RecordListRecyclerViewAdapter.MyViewHolder>() {
    var hideItem = true

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var itemlabel: TextView = itemview.findViewById(R.id.tv_label)
        var itemblock: ImageView = itemview.findViewById(R.id.iv_block)
        var itemtitle: TextView = itemview.findViewById(R.id.tv_title)
        var itemdate: TextView = itemview.findViewById(R.id.tv_date)
        var linearCheckBox: LinearLayout = itemview.findViewById(R.id.linear_checkbox)
        var checkBox: MaterialCheckBox = itemview.findViewById(R.id.checkbox_record)
        var linear: LinearLayout=itemview.findViewById(R.id.linear_record_box)
        var dash: ImageView = itemview.findViewById(R.id.iv_dash)
    }

    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(viewgroup.context).inflate(R.layout.item_read_record, viewgroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemlabel.text = (position+1).toString()
        holder.itemtitle.text = titles[position]
        holder.itemdate.text = dates[position]

//        if(position==0){
//            Log.e("label","${holder.itemlabel.text}")
//            holder.dash.layoutParams.height=46
//        }

        if(position<9){
            holder.itemblock.setImageResource(R.drawable.img_number_block_01_red)
        }
        else if(position<44){
            holder.itemblock.setImageResource(R.drawable.img_number_block_02_orange)
        }
        else if(position<99){
            holder.itemblock.setImageResource(R.drawable.img_number_block_03_yellow)
        }
        else if(position<449){
            holder.itemblock.setImageResource(R.drawable.img_number_block_04_green)
        }
        else if(position<999){
            holder.itemblock.setImageResource(R.drawable.img_number_block_06_sky)
        }

        if(hideItem){
            holder.linearCheckBox.visibility=View.GONE
        }
        else{
            holder.linearCheckBox.visibility=View.VISIBLE
        }

        holder.linear.setOnClickListener {
            var intent= Intent(holder.itemView.context, DetailRecordActivity::class.java)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return titles.size
    }

}