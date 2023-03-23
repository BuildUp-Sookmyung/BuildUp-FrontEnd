package com.example.buildupfrontend.record

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityReadActivityBinding

class ReadActivityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadActivityBinding
    private lateinit var recordAdapter: RecordListRecyclerViewAdapter
    var titles = arrayOf("첫 번째 멘토링", "두 번째 멘토링", "세 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링")
    var dates = arrayOf("2023-01-27", "2023-01-26", "2023-01-25", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pbActivity.progress = 25

        setSupportActionBar(binding.toolbarReadActivity)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout=true
        layoutManager.stackFromEnd=true
        binding.rvCardRecord.layoutManager = layoutManager
        recordAdapter=RecordListRecyclerViewAdapter(this, titles, dates)
        binding.rvCardRecord.adapter = recordAdapter

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.linearTitleActivity.setOnClickListener {
            var intent= Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }

        binding.tvDeleteCheck.setOnClickListener {
            var size = titles.size - 1
            if(binding.tvDeleteCheck.text=="선택 삭제") {
                binding.tvDeleteCheck.text = "취소"
                binding.tvCompleteDelete.visibility=View.VISIBLE

                recordAdapter.hideItem=false
                recordAdapter.notifyDataSetChanged()
            }
            else{
                binding.tvDeleteCheck.text = "선택 삭제"
                binding.tvCompleteDelete.visibility=View.GONE

                recordAdapter.hideItem=true
                recordAdapter.notifyDataSetChanged()
            }
        }

        binding.tvCompleteDelete.setOnClickListener {

        }

        binding.btnAddList.setOnClickListener {
            var intent=Intent(this,WriteRecordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.read_activity_menu,
            menu
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_activity -> {
                val mDialogView = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_delete, null)
                val mAlertDialog = AlertDialog.Builder(this, R.style.DetailDialog)
                    .setView(mDialogView)
                    .show()
                // Dialog button control
                val btnDelete =
                    mDialogView.findViewById<AppCompatButton>(R.id.btn_delete)
                val btnCancel =
                    mDialogView.findViewById<AppCompatButton>(R.id.btn_cancel)
                val btnClose =
                    mDialogView.findViewById<AppCompatButton>(R.id.btn_close)
                btnDelete.setOnClickListener {

                }
                btnCancel.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                btnClose.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}