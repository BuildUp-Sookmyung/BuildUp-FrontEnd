package com.example.buildupfrontend.record

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R
import com.example.buildupfrontend.retrofit.Client.RecordService
import com.example.buildupfrontend.retrofit.Response.RecordList
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import com.google.android.material.checkbox.MaterialCheckBox
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class RecordListRecyclerViewAdapter(
    private var context: Context,
    private val readActivityActivity: ReadActivityActivity,
    private val dataList: ArrayList<RecordList>,
    private var activityName: String,
    private var categoryName: String
) : RecyclerView.Adapter<RecordListRecyclerViewAdapter.MyViewHolder>() {
    var hideItem = true
    var checkList= ArrayList<Boolean>(Collections.nCopies(dataList.size, false))
    var idList= arrayListOf<Long>()

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
        holder.itemtitle.text = dataList[position].title
        holder.itemdate.text = dataList[position].date

        if(position==0){
            Log.e("label","${holder.itemlabel.text}")
            holder.dash.setImageResource(R.drawable.ic_dash_line_short)
        }

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

        holder.checkBox.isChecked = checkList[position]
        holder.checkBox.setOnCheckedChangeListener{ buttonView,isChecked->
            checkList[position]=isChecked
            Log.e("checkList","${position}")
        }

        holder.linear.setOnClickListener {
            var intent= Intent(holder.itemView.context, DetailRecordActivity::class.java)
            intent.putExtra("recordId",dataList[position].recordId)
            intent.putExtra("activityName",activityName)
            intent.putExtra("categoryName",categoryName)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun uncheckAll(){
        checkList.fill(false)
        notifyDataSetChanged()
    }

    fun deleteCheckedRecord(){
        for(i in dataList.size-1 downTo 0){
            if(checkList[i]){
                idList.add(dataList[i].recordId)
                dataList.removeAt(i)
            }
        }

        RecordService.retrofitDeleteRecord(idList).enqueue(object: Callback<SimpleResponse> {
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    notifyDataSetChanged()
                    if(dataList.size==0)
                        readActivityActivity.checkRecord(false)
                    Toast.makeText(context,"선택한 기록이 삭제되었습니다.",Toast.LENGTH_LONG).show()
                }else {
                    try {
                        val body = response.errorBody()!!.string()

                        Log.e(ContentValues.TAG, "body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }

}