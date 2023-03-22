package com.example.buildupfrontend.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.FragmentRecordBinding

class RecordFragment : Fragment() {
    private lateinit var binding:FragmentRecordBinding
    private lateinit var recordRecyclerViewDataList:ArrayList<RecordRecyclerViewData>
    private lateinit var activityRecyclerViewDataList: ArrayList<ActivityListRecyclerViewData>

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
            startActivity(intent)
        }

        binding.tvEdit.setOnClickListener{
            val intent=Intent(requireContext(), EditCategoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddActivity.setOnClickListener {
            var intent=Intent(requireContext(), WriteActivityActivity::class.java)
            startActivity(intent)
        }

        recordRecyclerViewDataList= arrayListOf()
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_all_nor, "전체"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_puzzle_nor, "대외활동"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_trophy_nor, "공모전"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_badge_nor, "자격증"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_school_nor, "교내활동"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_group_nor, "동아리"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_bulb_nor, "프로젝트"))

        binding.recyclerviewRecord.apply{
            layoutManager=GridLayoutManager(requireContext(), 7,GridLayoutManager.VERTICAL, false)
            adapter=RecordRecyclerViewAdapter(requireContext(),recordRecyclerViewDataList)
        }

        activityRecyclerViewDataList= arrayListOf(
            ActivityListRecyclerViewData("4", "국제 커뮤니케이션디자인 공모전","2023-01-24 ~ 2023-02-17"),
            ActivityListRecyclerViewData("75", "KB 라스쿨","2022-01-24 ~ 2023-02-14"),
            ActivityListRecyclerViewData("100", "희망재단 IT 서포터즈","2022-02-01 ~ 2022-06-20"),
        )

        binding.recyclerviewReadActivity.apply{
            layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter=ActivityListRecyclerViewAdapter(requireContext(), activityRecyclerViewDataList)
        }

//        binding.recyclerviewRecord[0].findViewById<>()d
    }
}