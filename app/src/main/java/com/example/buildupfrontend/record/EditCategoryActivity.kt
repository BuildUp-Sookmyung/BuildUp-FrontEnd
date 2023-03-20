package com.example.buildupfrontend.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        binding.btnBack.setOnClickListener {
            finish()
        }

        editCategoryRecyclerViewDataList= arrayListOf()
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_puzzle_nor, "대외활동"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_trophy_nor, "공모전"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_badge_nor, "자격증"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_school_nor, "교내활동"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_group_nor, "동아리"))
        editCategoryRecyclerViewDataList.add(RecordRecyclerViewData(R.drawable.ic_category_bulb_nor, "프로젝트"))

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
            R.id.add_category -> {
                val dialog=CategoryDialog(this)
                dialog.show()
                dialog.setOnClickListener(object: CategoryDialog.OnDialogClickListener{
                    override fun onClicked(name: String, id: Int) {

                    }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }
}