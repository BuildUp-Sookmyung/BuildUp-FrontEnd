package com.example.buildupfrontend.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityEditCategoryBinding

class EditCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBinding
    private lateinit var editCategoryRecyclerViewDataList:ArrayList<RecordRecyclerViewData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarEditCategory)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)
        toolbar.setDisplayHomeAsUpEnabled(true)

        editCategoryRecyclerViewDataList= arrayListOf()
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_outside_activity, "대외활동"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_competition, "공모전"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_certificate, "자격증"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_school_activity, "교내활동"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_club, "동아리"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_project, "프로젝트"))

        binding.recyclerviewEditCategory.apply{
            layoutManager= GridLayoutManager(context, editCategoryRecyclerViewDataList.size,GridLayoutManager.HORIZONTAL, false)
            adapter=EditCategoryRecyclerViewAdapter(context,editCategoryRecyclerViewDataList)
        }
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
            android.R.id.home -> finish()
            R.id.add_category -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}