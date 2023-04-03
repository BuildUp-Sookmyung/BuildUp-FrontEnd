package com.example.buildupfrontend.record

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.GlobalApplication
import com.example.buildupfrontend.databinding.FragmentRecordBinding
import com.example.buildupfrontend.retrofit.Client.ActivityService
import com.example.buildupfrontend.retrofit.Client.CategoryService
import com.example.buildupfrontend.retrofit.Response.ActivityCategoryResponse
import com.example.buildupfrontend.retrofit.Response.ActivityMeResponse
import com.example.buildupfrontend.retrofit.Response.CategoryInfo
import com.example.buildupfrontend.retrofit.Response.GetCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RecordFragment : Fragment() {
    private lateinit var binding:FragmentRecordBinding
    private lateinit var categoryRecyclerViewDataList:ArrayList<CategoryRecyclerViewData>
    private lateinit var activityRecyclerViewDataList: ArrayList<ActivityListRecyclerViewData>
    private var categoryList= arrayListOf<String>()
    private var categoryIdList= arrayListOf<Int>()
    private var iconIdList= arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRecordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        loadCategory()

        activityRecyclerViewDataList= arrayListOf()
        loadActivity()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linearWriteActivity.setOnClickListener {
            val intent= Intent(requireContext(),WriteActivityActivity::class.java)
            intent.putStringArrayListExtra("categoryName", categoryList)
            intent.putIntegerArrayListExtra("categoryId",categoryIdList)
            startActivity(intent)
        }

        binding.tvEdit.setOnClickListener{
            val intent=Intent(requireContext(), EditCategoryActivity::class.java)
            intent.putStringArrayListExtra("categoryName", categoryList)
            intent.putIntegerArrayListExtra("categoryId",categoryIdList)
            intent.putIntegerArrayListExtra("iconId",iconIdList)
            startActivity(intent)
        }

        binding.btnAddActivity.setOnClickListener {
            var intent=Intent(requireContext(), WriteActivityActivity::class.java)
            intent.putStringArrayListExtra("categoryName", categoryList)
            intent.putIntegerArrayListExtra("categoryId",categoryIdList)
            startActivity(intent)
        }

    }

    private fun loadCategory() {
//        val data = withContext(Dispatchers.IO) {
            CategoryService.retrofitGetCategory().enqueue(object : Callback<GetCategoryResponse> {
                override fun onResponse(
                    call: Call<GetCategoryResponse>,
                    response: Response<GetCategoryResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e("log", response.toString())
                        Log.e("log", response.body().toString())

                        categoryRecyclerViewDataList= arrayListOf()
                        categoryRecyclerViewDataList.add(CategoryRecyclerViewData(0, "전체",0))

                        val size = response.body()?.response?.size
                        for (i: Int in 0 until size!!) {
                            val categoryName = response.body()?.response?.get(i)?.categoryName
                            val iconId = response.body()?.response?.get(i)?.iconId
                            val categoryId = response.body()?.response?.get(i)?.categoryId

                            categoryList.add(categoryName!!)
                            categoryIdList.add(categoryId!!)
                            iconIdList.add(iconId!!)
//                            Log.e("record category","$iconId $categoryId $categoryName")
                            categoryRecyclerViewDataList.add(CategoryRecyclerViewData(iconId, categoryName, categoryId))
                        }
                        GlobalApplication.prefs.setIntegerList("categoryIdList", categoryIdList)
//                    Log.e("shared preference categoryIdList", "${GlobalApplication.prefs.getIntegerList("categoryIdList",0)}")
                        GlobalApplication.prefs.setStringList("categoryList", categoryList)
//                    Log.e("shared preference categoryList", "${GlobalApplication.prefs.getStringList("categoryList",0)}")



                        Log.e("categoryList","${categoryRecyclerViewDataList}")
                        binding.recyclerviewRecord.apply {
                            layoutManager = GridLayoutManager(context, categoryRecyclerViewDataList.size,
                                GridLayoutManager.VERTICAL, false)
                            adapter = CategoryRecyclerViewAdapter(requireContext(),this@RecordFragment, categoryRecyclerViewDataList)
                        }

                    } else {
                        try {
                            val body = response.errorBody()!!.string()

                            Log.e(ContentValues.TAG, "body : $body")

//                            emptyList
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                }

                override fun onFailure(call: Call<GetCategoryResponse>, t: Throwable) {
                    Log.e("TAG", "실패원인: {$t}")
//                    emptyList
                }
            })
//        }
//        return data
    }

    fun loadActivity(){
        ActivityService.retrofitActivityMe().enqueue(object: Callback<ActivityMeResponse> {
            override fun onResponse(
                call: Call<ActivityMeResponse>,
                response: Response<ActivityMeResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    val size = response.body()?.response?.size?.minus(1)
                    if(size==-1){
                        binding.scrollviewActivity.visibility=View.GONE
                        binding.linearActivityNone.visibility=View.VISIBLE
                    }
                    else{
                        binding.scrollviewActivity.visibility=View.VISIBLE
                        binding.linearActivityNone.visibility=View.GONE

                        val dataList=response.body()?.response
                        binding.recyclerviewReadActivity.apply{
                            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                            adapter=ActivityListRecyclerViewAdapter(requireContext(), dataList!!)
                        }
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

            override fun onFailure(call: Call<ActivityMeResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }

    fun loadCategoryActivity(categoryId: Int){
        ActivityService.retrofitCategoryActivities(categoryId).enqueue(object: Callback<ActivityCategoryResponse> {
            override fun onResponse(
                call: Call<ActivityCategoryResponse>,
                response: Response<ActivityCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    val size = response.body()?.response?.size?.minus(1)
                    if(size==-1){
                        binding.scrollviewActivity.visibility=View.GONE
                        binding.linearActivityNone.visibility=View.VISIBLE
                    }
                    else{
                        binding.scrollviewActivity.visibility=View.VISIBLE
                        binding.linearActivityNone.visibility=View.GONE

                        val dataList=response.body()?.response
                        binding.recyclerviewReadActivity.apply{
                            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                            adapter=ActivityListRecyclerViewAdapter(requireContext(), dataList!!)
                        }
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

            override fun onFailure(call: Call<ActivityCategoryResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }
}