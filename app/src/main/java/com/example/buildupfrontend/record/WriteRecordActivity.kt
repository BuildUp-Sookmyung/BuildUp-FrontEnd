package com.example.buildupfrontend.record

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.buildupfrontend.databinding.ActivityWriteRecordBinding

class WriteRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteRecordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWriteRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarRecord)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)
        toolbar.setDisplayHomeAsUpEnabled(true)

        val categoryList=listOf("대외활동","공모전","자격증","교내활동","동아리","프로젝트","카테고리를 선택해주세요.")
        val categoryAdapter=object: ArrayAdapter<String>(this@WriteRecordActivity, R.layout.simple_list_item_1, categoryList){
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
//        categoryAdapter.addAll(categoryList.toMutableList())
        binding.spinnerCategory.adapter=categoryAdapter
        binding.spinnerCategory.setSelection(categoryAdapter.count)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}