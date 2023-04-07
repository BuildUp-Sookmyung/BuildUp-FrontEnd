package com.example.buildupfrontend.record

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityReadActivityBinding
import com.example.buildupfrontend.iconList
import com.example.buildupfrontend.retrofit.Client.ActivityService
import com.example.buildupfrontend.retrofit.Client.RecordService
import com.example.buildupfrontend.retrofit.Response.ActivityRecordResponse
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ReadActivityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadActivityBinding
    private lateinit var recordAdapter: RecordListRecyclerViewAdapter
    private var activityId:Long=0
    private var recordIdList= arrayListOf<Long>()
    private var activityName:String=""
    private var categoryName:String=""
    private lateinit var checkedList: ArrayList<Boolean>
    private var categoryList= arrayListOf("대외활동","공모전","교내활동","동아리","프로젝트")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pbActivity.progress = 25

        setSupportActionBar(binding.toolbarReadActivity)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnBack.setOnClickListener {
            finish()
        }

        activityId=intent.getLongExtra("activityId",0)
        var percentage=intent.getIntExtra("percentage",0)
        activityName= intent.getStringExtra("activityName").toString()
        var date=intent.getStringExtra("date")
        categoryName= intent.getStringExtra("categoryName").toString()
        Log.e("percentage","$percentage")

        binding.pbActivity.progress= percentage
        binding.tvProgress.text="$percentage%"
        binding.tvTitle.text=activityName
        binding.tvDate.text=date
        binding.tvCategory.text=categoryName

        binding.linearTitleActivity.setOnClickListener {
            var intent= Intent(this, DetailActivity::class.java)
            intent.putExtra("activityId", activityId)
            startActivity(intent)
        }

        binding.tvDeleteCheck.setOnClickListener {
            var size = recordIdList.size - 1
            if(binding.tvDeleteCheck.text=="선택 삭제") {
                binding.tvDeleteCheck.text = "취소"
                binding.tvCompleteDelete.visibility=View.VISIBLE

                recordAdapter.hideItem=false
                recordAdapter.notifyDataSetChanged()
            }
            else{
                binding.tvDeleteCheck.text = "선택 삭제"
                binding.tvCompleteDelete.visibility=View.GONE

                recordAdapter.uncheckAll()
                recordAdapter.hideItem=true
                recordAdapter.notifyDataSetChanged()
            }
        }

        binding.tvCompleteDelete.setOnClickListener {
            val mDialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_delete_record, null)
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
                recordAdapter.deleteCheckedRecord()
                mAlertDialog.dismiss()
            }
            btnCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
            btnClose.setOnClickListener {
                mAlertDialog.dismiss()
            }

        }

        binding.btnAddList.setOnClickListener {
            lateinit var intent:Intent
            if(categoryList.contains(categoryName)) {
                intent = Intent(this, WriteRecordActivity::class.java)
            }
            else {
                intent = Intent(this, WriteOtherRecordActivity::class.java)
            }
            intent.putExtra("activityId",activityId)
            intent.putExtra("categoryName",categoryName)
            startActivity(intent)
        }

        binding.linearWriteActivity.setOnClickListener {
            lateinit var intent:Intent
            if(categoryList.contains(categoryName)) {
                intent = Intent(this, WriteRecordActivity::class.java)
            }
            else {
                intent = Intent(this, WriteOtherRecordActivity::class.java)
            }
            intent.putExtra("activityId",activityId)
            intent.putExtra("categoryName",categoryName)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadRecordList()

    }

    private fun loadRecordList(){
        RecordService.retrofitActivityRecord(activityId).enqueue(object:
            Callback<ActivityRecordResponse>{
            override fun onResponse(
                call: Call<ActivityRecordResponse>,
                response: Response<ActivityRecordResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    val size = response.body()?.response?.size?.minus(1)

                    if(size==-1){
                        checkRecord(false)
                    }
                    else {
                        checkRecord(true)

                        val dataList = response.body()?.response
                        val layoutManager = LinearLayoutManager(this@ReadActivityActivity)
                        layoutManager.reverseLayout = true
                        layoutManager.stackFromEnd = true
                        binding.rvCardRecord.layoutManager = layoutManager
                        recordAdapter =
                            RecordListRecyclerViewAdapter(this@ReadActivityActivity,this@ReadActivityActivity, dataList!!, activityName, categoryName)
                        binding.rvCardRecord.adapter = recordAdapter

                    }
                }else {
                    try {
                        val body = response.errorBody()!!.string()

                        Log.e(ContentValues.TAG, "body : $body")
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<ActivityRecordResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
            })
    }

    private fun deleteActivity(){
        ActivityService.retrofitDeleteActivity(activityId).enqueue(object:Callback<SimpleResponse>{
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    Toast.makeText(this@ReadActivityActivity,"활동이 삭제되었습니다.",Toast.LENGTH_LONG).show()
                    finish()
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
            }
        })
    }

    fun checkRecord(check:Boolean){
        if(check) {
            binding.linearRecordExist.visibility = View.VISIBLE
            binding.linearRecordNone.visibility = View.GONE
        }else{
            binding.linearRecordExist.visibility = View.GONE
            binding.linearRecordNone.visibility = View.VISIBLE
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
                    deleteActivity()
                    mAlertDialog.dismiss()
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