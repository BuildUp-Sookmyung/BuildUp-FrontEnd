package com.example.buildupfrontend.record

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityDetailRecordBinding
import com.example.buildupfrontend.retrofit.Client.RecordService
import com.example.buildupfrontend.retrofit.Response.RecordDetail
import com.example.buildupfrontend.retrofit.Response.RecordDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class DetailRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecordBinding
    private var recordId: Long=0
    private lateinit var detailRecord: RecordDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarRecordDetail)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnBack.setOnClickListener {
            finish()
        }

        recordId=intent.getLongExtra("recordId",0)
        var activityName=intent.getStringExtra("activityName")
        var categoryName=intent.getStringExtra("categoryName")

        binding.tvActivityDetailrecord.text=activityName
        binding.tvCategoryDetailrecord.text=categoryName

        loadRecordDetail()

    }

    private fun loadRecordDetail(){
        RecordService.retrofitRecordDetail(recordId).enqueue(object: Callback<RecordDetailResponse> {
            override fun onResponse(
                call: Call<RecordDetailResponse>,
                response: Response<RecordDetailResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    detailRecord=response.body()?.response!!

                    var date=detailRecord?.date
                    var title=detailRecord?.title
                    var experience=detailRecord?.experience
                    var concept=detailRecord?.concept
                    var result=detailRecord?.result
                    var content=detailRecord?.content
                    var imgUrls=detailRecord?.imgUrls
                    var url=detailRecord?.url

                    binding.tvDateDetailrecord.text=date
                    binding.tvTitleDetailrecord.text=title
                    binding.tvExperienceDetailrecord.text=experience
                    binding.tvConceptDetailrecord.text=concept
                    binding.tvResultDetailrecord.text=result
                    if(content=="")
                        binding.linearContent.visibility= View.GONE
                    else
                        binding.tvContentDetailrecord.text=content
                    if(url=="")
                        binding.linearUrlRecord.visibility=View.GONE
                    else
                        binding.tvUrlDetailrecord.text= url

                    binding.recyclerviewDetailRecord.apply{
                        layoutManager= LinearLayoutManager(this@DetailRecordActivity, LinearLayoutManager.HORIZONTAL,false)
                        adapter=RecordDetailImageRecyclerViewAdapter(this@DetailRecordActivity, imgUrls!!)
                    }
                }
            else {
                try {
                    val body = response.errorBody()!!.string()

                    Log.e(ContentValues.TAG, "body : $body")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                }
            }
            override fun onFailure(call: Call<RecordDetailResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: {$t}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.activity_detail_menu,
            menu
        )
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_activity -> {
                var intent= Intent(this,EditRecordActivity::class.java)

                intent.putExtra("recordId",detailRecord.recordId)
                intent.putExtra("date",detailRecord.date)
                intent.putExtra("title",detailRecord.title)
                intent.putExtra("experience",detailRecord.experience)
                intent.putExtra("concept",detailRecord.concept)
                intent.putExtra("concept",detailRecord.concept)
                intent.putExtra("content",detailRecord.content)
                intent.putStringArrayListExtra("imgUrls",detailRecord.imgUrls)
                intent.putExtra("url", detailRecord.url)

                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}