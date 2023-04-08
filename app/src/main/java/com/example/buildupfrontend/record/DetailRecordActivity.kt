package com.example.buildupfrontend.record

import android.content.ContentValues
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    private lateinit var categoryName:String
    private var categoryList= arrayListOf("대외활동","공모전","자격증","교내활동","동아리","프로젝트")

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
        categoryName= intent.getStringExtra("categoryName").toString()

        binding.tvActivityDetailrecord.text=activityName
        binding.tvCategoryDetailrecord.text=categoryName
    }

    override fun onResume() {
        super.onResume()
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

                    if(experience=="")
                        binding.linearExperience.visibility=View.GONE
                    else {
                        binding.linearExperience.visibility=View.VISIBLE
                        binding.tvExperienceDetailrecord.text = experience
                    }
                    if(concept=="")
                        binding.linearSolution.visibility=View.GONE
                    else {
                        binding.linearSolution.visibility=View.VISIBLE
                        binding.tvConceptDetailrecord.text = concept
                    }
                    if(result=="")
                        binding.linearResult.visibility=View.GONE
                    else{
                        binding.linearResult.visibility=View.VISIBLE
                        binding.tvResultDetailrecord.text=result
                    }
                    if(content=="")
                        binding.linearContent.visibility= View.GONE
                    else {
                        binding.linearContent.visibility= View.VISIBLE
                        binding.tvContentDetailrecord.text = content
                    }
                    if(url=="")
                        binding.linearUrlRecord.visibility=View.GONE
                    else {
                        binding.linearUrlRecord.visibility=View.VISIBLE
                        binding.tvUrlDetailrecord.text = url
                    }

                    var uriList= arrayListOf<String>()
                    for(i in 0 until imgUrls.size){
                        if(imgUrls[i]!=null)
                            uriList.add(imgUrls[i])
                    }

                    binding.recyclerviewDetailRecord.apply{
                        layoutManager= LinearLayoutManager(this@DetailRecordActivity, LinearLayoutManager.HORIZONTAL,false)
                        adapter=RecordDetailImageRecyclerViewAdapter(this@DetailRecordActivity, uriList)
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
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
                lateinit var intent: Intent
                if(categoryList.contains(categoryName))
                    intent= Intent(this,EditRecordActivity::class.java)
                else
                    intent= Intent(this,EditOtherRecordActivity::class.java)

                intent.putExtra("recordId",detailRecord.recordId)
                intent.putExtra("date",detailRecord.date)
                intent.putExtra("title",detailRecord.title)
                intent.putExtra("experience",detailRecord.experience)
                intent.putExtra("concept",detailRecord.concept)
                intent.putExtra("result",detailRecord.result)
                intent.putExtra("content",detailRecord.content)
                intent.putStringArrayListExtra("imgUrls",detailRecord.imgUrls)
                intent.putExtra("url", detailRecord.url)

                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}