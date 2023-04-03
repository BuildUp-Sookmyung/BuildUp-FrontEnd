package com.example.buildupfrontend.record

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R
import com.example.buildupfrontend.retrofit.Response.ActivityMeCheck

class ActivityListRecyclerViewAdapter(
    private var context: Context,
    private var dataList: ArrayList<ActivityMeCheck>
): RecyclerView.Adapter<ActivityListRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var percent: TextView = itemView.findViewById(R.id.tv_progress)
        var progressBar: ProgressBar=itemView.findViewById(R.id.pb_activity)
        var title: TextView=itemView.findViewById(R.id.tv_title)
        var date: TextView=itemView.findViewById(R.id.tv_date)
        var linear: LinearLayout=itemView.findViewById(R.id.linear_activity)

        fun bind(data: ActivityMeCheck){
            percent.text="${data.percentage}%"
            progressBar.progress=(data.percentage).toInt()
            title.text=data.activityName
            date.text="${data.startDate} ~ ${data.endDate}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityListRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_read_activity,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityListRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(dataList[position])

        holder.linear.setOnClickListener {
            val date="${dataList[position].startDate} ~ ${dataList[position].endDate}"
            var intent= Intent(holder.itemView.context, ReadActivityActivity::class.java)
            intent.putExtra("activityId",dataList[position].activityId)
            intent.putExtra("percentage", dataList[position].percentage)
            intent.putExtra("activityName", dataList[position].activityName)
            intent.putExtra("date", date)
            intent.putExtra("categoryName", dataList[position].categoryName)
//            Log.e("percentage","${dataList[position].percentage}")
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}