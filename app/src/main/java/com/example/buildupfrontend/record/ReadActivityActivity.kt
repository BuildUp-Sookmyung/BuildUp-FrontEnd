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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityReadActivityBinding
import com.example.buildupfrontend.iconList
import com.example.buildupfrontend.retrofit.Client.RecordService
import com.example.buildupfrontend.retrofit.Response.ActivityRecordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ReadActivityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadActivityBinding
    private lateinit var recordAdapter: RecordListRecyclerViewAdapter
    private var activityId:Long=0
    private var recordIdList= arrayListOf<Long>()
    private var titleList= arrayListOf<String>()
    private var dateList= arrayListOf<String>()
//    var titles = arrayOf("첫 번째 멘토링", "두 번째 멘토링", "세 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링", "네 번째 멘토링")
//    var dates = arrayOf("2023-01-27", "2023-01-26", "2023-01-25", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24", "2023-01-24")


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
        var activityName=intent.getStringExtra("activityName")
        var date=intent.getStringExtra("date")
        Log.e("percentage","$percentage")

        binding.pbActivity.progress= percentage
        binding.tvProgress.text="$percentage%"
        binding.tvTitle.text=activityName
        binding.tvDate.text=date

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

                recordAdapter.hideItem=true
                recordAdapter.notifyDataSetChanged()
            }
        }

        binding.tvCompleteDelete.setOnClickListener {

        }

        binding.btnAddList.setOnClickListener {
            var intent=Intent(this,WriteRecordActivity::class.java)
            intent.putExtra("activityId",activityId)
            startActivity(intent)
        }

        binding.linearWriteActivity.setOnClickListener {
            var intent=Intent(this,WriteRecordActivity::class.java)
            intent.putExtra("activityId",activityId)
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
                        binding.linearRecordExist.visibility=View.GONE
                        binding.linearRecordNone.visibility=View.VISIBLE
                    }
                    else {
                        binding.linearRecordExist.visibility = View.VISIBLE
                        binding.linearRecordNone.visibility = View.GONE

                        val dataList = response.body()?.response
                        val layoutManager = LinearLayoutManager(this@ReadActivityActivity)
                        layoutManager.reverseLayout = true
                        layoutManager.stackFromEnd = true
                        binding.rvCardRecord.layoutManager = layoutManager
                        recordAdapter =
                            RecordListRecyclerViewAdapter(this@ReadActivityActivity, dataList!!)
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