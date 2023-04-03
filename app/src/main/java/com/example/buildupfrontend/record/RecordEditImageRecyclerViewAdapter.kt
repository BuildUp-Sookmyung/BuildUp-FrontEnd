package com.example.buildupfrontend.record

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.buildupfrontend.R

class RecordEditImageRecyclerViewAdapter(
    val context: Context,
    private val dataList: ArrayList<String>
): RecyclerView.Adapter<RecordEditImageRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var deleteImg: ImageView=itemView.findViewById(R.id.iv_delete_image)
        var image: ImageView = itemView.findViewById(R.id.iv_write_record)
        var linear: LinearLayout = itemView.findViewById(R.id.linear_represent)

        fun bind(data: String) {
            Glide.with(context)
                .load(data)
                .fitCenter()
                .apply(RequestOptions().override(500, 500))
                .into(image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordEditImageRecyclerViewAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecordEditImageRecyclerViewAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(dataList[position])

        holder.deleteImg.visibility=View.VISIBLE
        if(position==0)
            holder.linear.visibility=View.VISIBLE

        holder.deleteImg.setOnClickListener {
            dataList.removeAt(position)
            notifyItemChanged(position)

            var activity=holder.itemView.context as EditRecordActivity
            activity.updateImageList(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}