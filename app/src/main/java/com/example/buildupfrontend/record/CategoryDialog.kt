package com.example.buildupfrontend.record

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.CategoryDialogBinding

class CategoryDialog(context: Context): Dialog(context) {
    private lateinit var binding: CategoryDialogBinding
    private lateinit var addCategoryRecyclerViewDataList:ArrayList<AddCategoryRecyclerViewData>
    private lateinit var onClickListener: OnDialogClickListener
    private lateinit var recyclerview: RecyclerView

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    interface OnDialogClickListener
    {
        fun onClicked(name: String, id: Int)
    }

    private var page1DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_puzzle_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_trophy_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_badge_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_school_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_group_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_bulb_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_language_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_music_nor)
    )
    private var page2DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_workout_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_movie_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_reading_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_suitcase_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_major_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_internship_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_language_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_music_nor)
    )
    private var page3DataList=arrayListOf(
        AddCategoryRecyclerViewData(R.drawable.ic_category_academy_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_contest_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_medal_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_oversea_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_clock_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_presentation_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_meal_nor),
        AddCategoryRecyclerViewData(R.drawable.ic_category_briefcase_nor)
    )
    private var page=0

    init{
        binding=CategoryDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        addCategoryRecyclerViewDataList= page1DataList

        val viewPager2=binding.viewpagerAddCategory
        val viewpagerAdapter=AddCategoryViewpagerAdapter(context, page1DataList)
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
            if(cur==1){
                binding.ivArrowPrevious.setImageResource(R.drawable.btn_circle_arrow_previous_dis)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context, page1DataList)
                viewPager2.setCurrentItem(0)
            }
            else if(cur==2){
                binding.ivArrowNext.setImageResource(R.drawable.btn_circle_arrow_next_nor)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context, page2DataList)
                viewPager2.setCurrentItem(1)
            }
        }

        binding.ivArrowNext.setOnClickListener {
            var cur=viewPager2.currentItem
            if(cur==0){
                binding.ivArrowPrevious.setImageResource(R.drawable.btn_circle_arrow_previous_nor)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context, page2DataList)
                viewPager2.setCurrentItem(1)
            }
            else if(cur==1){
                binding.ivArrowNext.setImageResource(R.drawable.btn_circle_arrow_next_dis)
                viewPager2.adapter=AddCategoryViewpagerAdapter(context, page3DataList)
                viewPager2.setCurrentItem(2)
            }
        }

        binding.categoryDialogComplete.setOnClickListener {
            if(binding.etCategoryName.text.toString()==""){

            }
            else
                onClickListener.onClicked(binding.etCategoryName.text.toString(),1)
        }
    }
}