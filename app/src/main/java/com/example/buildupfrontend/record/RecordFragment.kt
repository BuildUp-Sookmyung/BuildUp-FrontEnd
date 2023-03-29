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
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.FragmentRecordBinding
import com.example.buildupfrontend.iconList
import com.example.buildupfrontend.retrofit.Client.ActivityService
import com.example.buildupfrontend.retrofit.Client.CategoryService
import com.example.buildupfrontend.retrofit.Response.ActivityMeResponse
import com.example.buildupfrontend.retrofit.Response.GetCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RecordFragment : Fragment() {
    private lateinit var binding:FragmentRecordBinding
    private lateinit var recordRecyclerViewDataList:ArrayList<RecordRecyclerViewData>
    private lateinit var activityRecyclerViewDataList: ArrayList<ActivityListRecyclerViewData>
    private var categoryList= arrayListOf<String>()
    private var categoryIdList= arrayListOf<Int>()
    private var iconIdList= arrayListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentRecordBinding.inflate(layoutInflater)
        return binding.root
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

        activityRecyclerViewDataList= arrayListOf()
        loadActivity()

        recordRecyclerViewDataList= arrayListOf()
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_all_nor, "전체"))
        loadCategory()

//        activityRecyclerViewDataList= arrayListOf(
//            ActivityListRecyclerViewData("4", "국제 커뮤니케이션디자인 공모전","2023-01-24 ~ 2023-02-17"),
//            ActivityListRecyclerViewData("75", "KB 라스쿨","2022-01-24 ~ 2023-02-14"),
//            ActivityListRecyclerViewData("100", "희망재단 IT 서포터즈","2022-02-01 ~ 2022-06-20"),
//        )
    }

    private fun loadCategory(){
        CategoryService.retrofitGetCategory().enqueue(object: Callback<GetCategoryResponse> {
            override fun onResponse(
                call: Call<GetCategoryResponse>,
                response: Response<GetCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    val size = response.body()?.response?.size?.minus(1)
                    for (i: Int in 0..size!!){
                        val categoryName=response.body()?.response?.get(i)?.categoryName
                        val iconId= response.body()?.response?.get(i)?.iconId
                        val categoryId=response.body()?.response?.get(i)?.categoryId

                        categoryList.add(categoryName!!)
                        categoryIdList.add(categoryId!!)
                        iconIdList.add(iconId!!)
                    }
                    GlobalApplication.prefs.setIntegerList("categoryIdList",categoryIdList)
//                    Log.e("shared preference categoryIdList", "${GlobalApplication.prefs.getIntegerList("categoryIdList",0)}")
                    GlobalApplication.prefs.setStringList("categoryList",categoryList)
//                    Log.e("shared preference categoryList", "${GlobalApplication.prefs.getStringList("categoryList",0)}")

                    val dataList=response.body()?.response
                    binding.recyclerviewRecord.apply{
                        layoutManager=GridLayoutManager(requireActivity(), 7,GridLayoutManager.VERTICAL, false)
                        adapter=RecordRecyclerViewAdapter(requireActivity(),dataList!!)
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

    private fun loadActivity(){
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
}