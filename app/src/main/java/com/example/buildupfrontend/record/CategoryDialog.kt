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
    private lateinit var onClickListener: CalendarDialog.OnDialogClickListener
    private lateinit var recyclerview: RecyclerView
    private var iconId=0
    private var selectedPage=-1
    private var selectedIcon=-1

    private var page1DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_puzzle_nor,1,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_trophy_nor,2,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_badge_nor,3,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_school_nor,4,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_group_nor,5,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_bulb_nor,6,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_language_nor,7,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_music_nor,8,"#575757")
    )
    private var page2DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_workout_nor,9,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_movie_nor,10,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_reading_nor,11,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_suitcase_nor,12,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_major_nor,13,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_internship_nor,14,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_language_nor,15,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_music_nor,16,"#575757")
    )
    private var page3DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_academy_nor,17,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_contest_nor,18,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_medal_nor,19,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_oversea_nor,20,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_clock_nor,21,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_presentation_nor,22,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_meal_nor,23,"#575757"),
        AddCategoryRecyclerViewData(R.drawable.ic_category_briefcase_nor,24,"#575757")
    )
    private var page=0

    init{
        binding=CategoryDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        addCategoryRecyclerViewDataList= page1DataList

        val viewPager2=binding.viewpagerAddCategory
        val viewpagerAdapter=AddCategoryViewpagerAdapter(context,this@CategoryDialog, page1DataList)
        viewPager2.adapter= viewpagerAdapter
        viewPager2.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        viewPager2.isUserInputEnabled=false

        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                page=position
                Log.e("page: ", "$page")
            }
        })

        binding.imgCategoryClose.setOnClickListener {
            dismiss()
        }

        binding.ivArrowPrevious.setOnClickListener {
            var cur=viewPager2.currentItem
            selectedIcon=0
            if(cur==1){
                binding.ivArrowPrevious.setImageResource(R.drawable.btn_circle_arrow_previous_dis)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context,this@CategoryDialog, page1DataList)
                viewPager2.setCurrentItem(0)
            }
            else if(cur==2){
                binding.ivArrowNext.setImageResource(R.drawable.btn_circle_arrow_next_nor)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context,this@CategoryDialog, page2DataList)
                viewPager2.setCurrentItem(1)
            }
        }

        binding.ivArrowNext.setOnClickListener {
            var cur=viewPager2.currentItem
            selectedIcon=0
            if(cur==0){
                binding.ivArrowPrevious.setImageResource(R.drawable.btn_circle_arrow_previous_nor)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context,this@CategoryDialog, page2DataList)
                viewPager2.setCurrentItem(1)
            }
            else if(cur==1){
                binding.ivArrowNext.setImageResource(R.drawable.btn_circle_arrow_next_dis)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context,this@CategoryDialog, page3DataList)
                viewPager2.setCurrentItem(2)
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
        CategoryService.retrofitPostCategory(CategoryRequest(binding.etCategoryName.text.toString(),8*selectedPage+selectedIcon+1)).enqueue(object:
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

    fun selectPage(): Int {
        return selectedPage
    }

    fun savePosition(pos: Int){
        selectedIcon=pos
        Log.e("savePosition","$selectedIcon")
    }

    fun selectPosition():Int{
        return selectedIcon
    }

    fun getCategoryId(id: Int){
        iconId=id
    }

    fun getCurrentPage():Int{
        val viewPager2=binding.viewpagerAddCategory
//        Log.e("currentPage","${viewPager2.currentItem}")
        return viewPager2.currentItem
    }

}