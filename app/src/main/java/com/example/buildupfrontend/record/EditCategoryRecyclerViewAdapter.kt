package com.example.buildupfrontend.record

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buildupfrontend.R
import com.example.buildupfrontend.iconList
import com.example.buildupfrontend.retrofit.Client.CategoryService
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EditCategoryRecyclerViewAdapter(
    private val context: Context,
    private val editCategoryActivity: EditCategoryActivity,
    private var dataList: ArrayList<ReadCategoryRecyclerViewData>
): RecyclerView.Adapter<EditCategoryRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var image: ImageView =itemView.findViewById(R.id.image_category)
        var category: TextView =itemView.findViewById(R.id.tv_category)
        var more: ImageView=itemView.findViewById(R.id.img_more)

        fun bind(data: ReadCategoryRecyclerViewData){
            image.setImageResource(data.image)
            category.text=data.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit_category,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])

        if(position>5){
            holder.more.visibility=View.VISIBLE
        }

        holder.more.setOnClickListener {
            showItemOption(holder.more,position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun showItemOption(view: View,position: Int){
        // showItemOptionsMenu 함수 내부에 다음 코드를 추가합니다.
        val popup = PopupWindow(context)
        val popupView = LayoutInflater.from(context).inflate(R.layout.item_category_popup, null)

        // 수정 버튼 클릭 시 실행될 함수
        popupView.findViewById<View>(R.id.btn_edit_category).setOnClickListener {
            var categoryId=dataList[position].categoryId.toString()
            var categoryName=dataList[position].category
            var iconId=dataList[position].iconId.toString()
            var data= arrayListOf<String>(categoryId,categoryName, iconId)
            Log.e("category Info","$data")
            val dialog=CategoryEditDialog(context,editCategoryActivity, data)
            dialog.show()
            popup.dismiss()
        }

        // 삭제 버튼 클릭 시 실행될 함수
        popupView.findViewById<View>(R.id.btn_delete_category).setOnClickListener {
            editCategory(position)
            popup.dismiss()
        }

        popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup.contentView = popupView
        popup.isOutsideTouchable = true
        popup.showAsDropDown(view, -250, 0)
    }

    private fun deleteCategory(pos: Int){
        CategoryService.retrofitDeleteCategory(dataList[pos].categoryId.toLong()).enqueue(object:
            Callback<SimpleResponse> {
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {if (response.isSuccessful) {
                Log.e("log", response.toString())
                Log.e("log", response.body().toString())

                dataList.removeAt(pos)
                notifyItemRemoved(pos)
//                notifyItemRangeChanged(pos, dataList.size)
            }
            else {
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

    private fun editCategory(pos:Int){
        val mDialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_delete_category, null)

        val categoryName=mDialogView.findViewById<TextView>(R.id.tv_category)
        categoryName.text="'${dataList[pos].category}'"

        val mAlertDialog = AlertDialog.Builder(context, R.style.DetailDialog)
            .setView(mDialogView)
            .show()
        // Dialog button control
        val btnDelete =
            mDialogView.findViewById<AppCompatButton>(R.id.btn_delete)
        val btnCancel =
            mDialogView.findViewById<AppCompatButton>(R.id.btn_cancel)
        val btnClose =
            mDialogView.findViewById<AppCompatButton>(R.id.btn_close)
        btnDelete.setOnClickListener {
            deleteCategory(pos)
            mAlertDialog.dismiss()
        }
        btnCancel.setOnClickListener {
            mAlertDialog.dismiss()
        }
        btnClose.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

}