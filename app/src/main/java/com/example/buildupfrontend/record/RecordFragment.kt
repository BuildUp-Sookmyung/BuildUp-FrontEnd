package com.example.buildupfrontend.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.FragmentRecordBinding

class RecordFragment : Fragment() {
    private lateinit var binding:FragmentRecordBinding
    private lateinit var recordRecyclerViewDataList:ArrayList<RecordRecyclerViewData>

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
            val intent= Intent(requireContext(),WriteRecordActivity::class.java)
            startActivity(intent)
        }

        binding.tvEdit.setOnClickListener{
            val intent=Intent(requireContext(), EditCategoryActivity::class.java)
            startActivity(intent)
        }

        recordRecyclerViewDataList= arrayListOf()
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_all, "전체"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_outside_activity, "대외활동"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_competition, "공모전"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_certificate, "자격증"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_school_activity, "교내활동"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_club, "동아리"))
        recordRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_project, "프로젝트"))

        binding.recyclerviewRecord.apply{
            layoutManager=GridLayoutManager(requireContext(), 7,GridLayoutManager.VERTICAL, false)
            adapter=RecordRecyclerViewAdapter(requireContext(),recordRecyclerViewDataList)
        }

//        binding.recyclerviewRecord[0].findViewById<>()d
    }
}