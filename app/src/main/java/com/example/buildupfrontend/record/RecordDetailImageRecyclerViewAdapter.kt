package com.example.buildupfrontend.record

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.buildupfrontend.R

class RecordDetailImageRecyclerViewAdapter(
    private val context: Context,
    private val dataList: ArrayList<String>
): RecyclerView.Adapter<RecordDetailImageRecyclerViewAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView =itemView.findViewById(R.id.iv_write_record)
        var linear: LinearLayout =itemView.findViewById(R.id.linear_represent)

        fun bind(data: String){
            Glide.with(context)
                .load(data)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .apply(RequestOptions().override(500,500))
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordDetailImageRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_record,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordDetailImageRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.bind(dataList[position])

        if(position==0)
            holder.linear.visibility= View.VISIBLE

        holder.image.setOnClickListener {
            var intent= Intent(holder.itemView.context, ShowImagesActivity::class.java)
            intent.putStringArrayListExtra("imageList",dataList)
            intent.putExtra("page",position)
            ContextCompat.startActivity(holder.itemView.context,intent,null)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}