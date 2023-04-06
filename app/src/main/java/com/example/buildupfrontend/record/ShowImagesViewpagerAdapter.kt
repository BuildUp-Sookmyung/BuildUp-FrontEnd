package com.example.buildupfrontend.record

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.buildupfrontend.R

class ShowImagesViewpagerAdapter(
    private val context: Context,
    private val showImagesActivity: ShowImagesActivity,
    private val dataList: ArrayList<String>,
    private val page: Int
    ): RecyclerView.Adapter<ShowImagesViewpagerAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(itemView: ViewGroup): RecyclerView.ViewHolder(itemView){
        var imageView=itemView.findViewById<ImageView>(R.id.iv_show_images)

        fun bind(data: String){
            Glide.with(context)
                .load(data)
                .fitCenter()
                .apply(RequestOptions().override(500, 500))
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show_images,parent,false)
        showImagesActivity.setViewpagerPage(page)
        return PagerViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}