package com.example.buildupfrontend.record

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityEditCategoryBinding
import com.example.buildupfrontend.iconList
import com.example.buildupfrontend.retrofit.Client.CategoryService
import com.example.buildupfrontend.retrofit.Response.GetCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EditCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBinding
    private lateinit var editCategoryRecyclerViewDataList:ArrayList<ReadCategoryRecyclerViewData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarEditCategory)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnBack.setOnClickListener {
            finish()
        }
//        val categoryIdList= intent.getIntegerArrayListExtra("categoryId") as ArrayList<Int>
//        val categoryList:ArrayList<String> = intent.getStringArrayListExtra("categoryName") as ArrayList<String>
//        val iconIdList:ArrayList<Int> = intent.getIntegerArrayListExtra("iconId") as ArrayList<Int>
//
//        val size=categoryList.size-1
//        for (i: Int in 0..size!!){
//            editCategoryRecyclerViewDataList.add(ReadCategoryRecyclerViewData(iconList[iconIdList[i]], categoryList[i], iconIdList[i], categoryIdList[i]))
//        }

//        binding.recyclerviewEditCategory.apply{
//            layoutManager= GridLayoutManager(context, editCategoryRecyclerViewDataList.size,GridLayoutManager.HORIZONTAL, false)
//            adapter=EditCategoryRecyclerViewAdapter(context,editCategoryRecyclerViewDataList)
//        }
    }

    override public fun onResume() {
        super.onResume()
        loadCategory()
    }

    private fun loadCategory(){
        CategoryService.retrofitGetCategory().enqueue(object: Callback<GetCategoryResponse>{
            override fun onResponse(
                call: Call<GetCategoryResponse>,
                response: Response<GetCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    editCategoryRecyclerViewDataList= arrayListOf()

                    val size = response.body()?.response?.size?.minus(1)
                    for (i: Int in 0..size!!){
                        val categoryName=response.body()?.response?.get(i)?.categoryName
                        val iconId= response.body()?.response?.get(i)?.iconId
                        val categoryId=response.body()?.response?.get(i)?.categoryId

                        editCategoryRecyclerViewDataList.add(ReadCategoryRecyclerViewData(iconList[iconId!!.toInt()], categoryName!!,iconId!!, categoryId!!))
                    }

                    binding.recyclerviewEditCategory.apply{
                        layoutManager= GridLayoutManager(context, editCategoryRecyclerViewDataList.size,GridLayoutManager.HORIZONTAL, false)
                        adapter=EditCategoryRecyclerViewAdapter(context, this@EditCategoryActivity,editCategoryRecyclerViewDataList)
                    }
                }else {
                    try {
                        val body = response.errorBody()!!.string()

                        Log.e(ContentValues.TAG, "body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<GetCategoryResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.edit_category_menu,
            menu
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_category -> {
                val dialog=CategoryDialog(this, this)
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}