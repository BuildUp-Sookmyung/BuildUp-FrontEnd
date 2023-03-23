package com.example.buildupfrontend.record

import android.R
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.buildupfrontend.databinding.ActivityWriteActivityBinding
import java.util.*

class WriteActivityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteActivityBinding
    private lateinit var dialog: CalendarDialog
    private lateinit var categoryText: String
    private lateinit var activityText: String
    private lateinit var dateStart: Date
    private lateinit var dateEnd: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWriteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarRecord)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val categoryList=listOf("대외활동","공모전","자격증","교내활동","동아리","프로젝트","카테고리를 선택해주세요.")
        val categoryAdapter=object: ArrayAdapter<String>(this@WriteActivityActivity, R.layout.simple_list_item_1, categoryList){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getView(position, convertView, parent)

                if (position == count) {
                    //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                    (v.findViewById<View>(R.id.text1) as TextView).text = ""
                    //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                    (v.findViewById<View>(R.id.text1) as TextView).hint = getItem(count)
                }

                return v
            }

            override fun getCount(): Int {
                //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
                return super.getCount() - 1
            }
        }

        binding.spinnerCategory.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryText=binding.spinnerCategory.selectedItem.toString()
                Log.e("categoryText", "$categoryText")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

//        categoryAdapter.addAll(categoryList.toMutableList())
        binding.spinnerCategory.adapter=categoryAdapter
        binding.spinnerCategory.setSelection(categoryAdapter.count)

        Log.e("category", "${binding.spinnerCategory.selectedItem}")

        datePick()
    }

    private fun datePick(){
        binding.linearCalendarStart.setOnClickListener {
            dialog= CalendarDialog(this)
            dialog.show()
            dialog.setOnClickListener(object: CalendarDialog.OnDialogClickListener{
                override fun onClicked(date: String) {
                    binding.tvCalendarStart.text=date
                    binding.tvCalendarStart.setTextColor(Color.parseColor("#262626"))
                }
            })
        }

        binding.linearCalendarEnd.setOnClickListener {
            dialog= CalendarDialog(this)
            dialog.show()
            dialog.setOnClickListener(object: CalendarDialog.OnDialogClickListener{
                override fun onClicked(date: String) {
                    binding.tvCalendarEnd.text=date
                    binding.tvCalendarEnd.setTextColor(Color.parseColor("#262626"))
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

        }
        return super.onOptionsItemSelected(item)
    }
}