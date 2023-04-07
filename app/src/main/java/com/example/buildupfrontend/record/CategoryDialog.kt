package com.example.buildupfrontend.record

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.CategoryDialogBinding
import com.example.buildupfrontend.retrofit.Client.CategoryService
import com.example.buildupfrontend.retrofit.Request.CategoryRequest
import com.example.buildupfrontend.retrofit.Request.EditCategoryRequest
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CategoryDialog(
    context: Context,
    private val editCategoryActivity: EditCategoryActivity
): Dialog(context) {
    private lateinit var binding: CategoryDialogBinding
    private lateinit var addCategoryRecyclerViewDataList:ArrayList<AddCategoryRecyclerViewData>
    private var selectedPage=-1
    private var selectedIcon=-1
    private var pos=-1

    private var page1DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_puzzle_nor,1),
        AddCategoryRecyclerViewData(R.drawable.ic_category_trophy_nor,2,),
        AddCategoryRecyclerViewData(R.drawable.ic_category_badge_nor,3),
        AddCategoryRecyclerViewData(R.drawable.ic_category_school_nor,4),
        AddCategoryRecyclerViewData(R.drawable.ic_category_group_nor,5),
        AddCategoryRecyclerViewData(R.drawable.ic_category_bulb_nor,6),
        AddCategoryRecyclerViewData(R.drawable.ic_category_language_nor,7),
        AddCategoryRecyclerViewData(R.drawable.ic_category_music_nor,8)
    )
    private var page2DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_workout_nor,9),
        AddCategoryRecyclerViewData(R.drawable.ic_category_movie_nor,10),
        AddCategoryRecyclerViewData(R.drawable.ic_category_reading_nor,11),
        AddCategoryRecyclerViewData(R.drawable.ic_category_suitcase_nor,12),
        AddCategoryRecyclerViewData(R.drawable.ic_category_major_nor,13),
        AddCategoryRecyclerViewData(R.drawable.ic_category_internship_nor,14),
        AddCategoryRecyclerViewData(R.drawable.ic_category_document_nor,15),
        AddCategoryRecyclerViewData(R.drawable.ic_category_interview_nor,16)
    )
    private var page3DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_academy_nor,17),
        AddCategoryRecyclerViewData(R.drawable.ic_category_contest_nor,18),
        AddCategoryRecyclerViewData(R.drawable.ic_category_medal_nor,19),
        AddCategoryRecyclerViewData(R.drawable.ic_category_oversea_nor,20),
        AddCategoryRecyclerViewData(R.drawable.ic_category_clock_nor,21),
        AddCategoryRecyclerViewData(R.drawable.ic_category_presentation_nor,22),
        AddCategoryRecyclerViewData(R.drawable.ic_category_meal_nor,23),
        AddCategoryRecyclerViewData(R.drawable.ic_category_briefcase_nor,24)
    )

    init{
        binding=CategoryDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        addCategoryRecyclerViewDataList=page1DataList

        val viewPager2=binding.viewpagerAddCategory
        val viewpagerAdapter=AddCategoryViewpagerAdapter(context)
        viewPager2.adapter= viewpagerAdapter
        viewPager2.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        viewPager2.isUserInputEnabled=false

        val recyclerview=binding.recyclerviewAddCategory
        val recyclerviewAdapter=AddCategoryRecyclerViewAdapter(this,addCategoryRecyclerViewDataList,pos)
        recyclerview.layoutManager=
            GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        recyclerview.adapter=recyclerviewAdapter

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                Log.e("page", "$position")
                Log.e("selectedIcon","$selectedIcon")
            }
        })

        binding.imgCategoryClose.setOnClickListener {
            dismiss()
        }

        binding.ivArrowPrevious.setOnClickListener {
            var cur=viewPager2.currentItem
            if(cur==1){
                binding.ivArrowPrevious.setImageResource(R.drawable.btn_circle_arrow_previous_dis)
                pos=-1
                if(selectedIcon in 1..8)
                    pos=selectedIcon-1
                recyclerviewAdapter.updateData(page1DataList,pos)
                viewPager2.currentItem = 0
            }
            else if(cur==2){
                binding.ivArrowNext.setImageResource(R.drawable.btn_circle_arrow_next_nor)
                pos=-1
                if(selectedIcon in 9..16)
                    pos=selectedIcon%8-1
                recyclerviewAdapter.updateData(page2DataList,pos)
                viewPager2.currentItem = 1
            }
        }

        binding.ivArrowNext.setOnClickListener {
            var cur=viewPager2.currentItem
            if(cur==0){
                binding.ivArrowPrevious.setImageResource(R.drawable.btn_circle_arrow_previous_nor)
                pos=-1
                if(selectedIcon in 9..16)
                    pos=selectedIcon%8-1
                recyclerviewAdapter.updateData(page2DataList,pos)
                viewPager2.currentItem = 1
            }
            else if(cur==1){
                binding.ivArrowNext.setImageResource(R.drawable.btn_circle_arrow_next_dis)
                pos=-1
                if(selectedIcon in 17..24)
                    pos=selectedIcon%8-1
                Log.e("pos","$pos")
                recyclerviewAdapter.updateData(page3DataList,pos)
                viewPager2.currentItem = 2
            }
        }

        binding.categoryDialogComplete.setOnClickListener {
            if(binding.etCategoryName.text.toString()==""){
                Toast.makeText(context,"카테고리명을 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else if(selectedIcon==-1){
                Toast.makeText(context,"아이콘을 선택해주세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                AddCategory()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith("android.webkit.")) {
            val rect = Rect()
            view.getGlobalVisibleRect(rect)
            if (!rect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                view.clearFocus()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun AddCategory(){
        CategoryService.retrofitPostCategory(CategoryRequest(binding.etCategoryName.text.toString(),selectedIcon)).enqueue(object:
            Callback<SimpleResponse>{
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    editCategoryActivity.onResume()
                    dismiss()
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
                Toast.makeText(context,"다시 한 번 시도해주세요.",Toast.LENGTH_LONG).show()
            }
            })
    }

    fun savePage(page: Int){
        selectedPage=page
        Log.e("savePage","$selectedPage")
    }

    fun saveIconId(id: Int){
        selectedIcon=id
        Log.e("savePosition","$selectedIcon")
    }

    fun getCurrentPage():Int{
        val viewPager2=binding.viewpagerAddCategory
        return viewPager2.currentItem
    }

}