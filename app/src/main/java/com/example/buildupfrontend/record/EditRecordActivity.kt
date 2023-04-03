package com.example.buildupfrontend.record

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityEditRecordBinding
import com.example.buildupfrontend.retrofit.Client.RecordService
import com.example.buildupfrontend.retrofit.Request.RecordRequest
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.cache2.Relay.Companion.edit
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class EditRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRecordBinding
    private lateinit var dialog: CalendarDialog
    private var recordId:Long =0
    private var dateValue="yyyy-mm-dd"
    private var titleValue: String = ""
    private var experienceValue: String = ""
    private var conceptValue: String = ""
    private var resultValue: String = ""
    private var contentValue: String = ""
    private var urlValue: String = ""
    private lateinit var imageFile: File
    private var imageUri= arrayListOf<Uri>()
    private var imageString= arrayListOf<String>()
    private var imgFile = arrayListOf<MultipartBody.Part>()
    private var REQ_GALLERY=1
    private var check=false
    private var changeImg=false

    val imageResult=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result->
        if(result.resultCode==RESULT_OK ){
            Log.e("result", "$result")
            val imageUris = arrayListOf<Uri>()
            var data=result.data
            if (data?.clipData != null) {
                // 다중 이미지 선택한 경우
                val clipData = data.clipData
                val count = minOf(clipData?.itemCount!!, 3-imageUri.size) // 최대 3개까지만 선택
                for (i in 0 until count) {
                    imageUris.add(clipData.getItemAt(i).uri)
                }
            } else {
                // 단일 이미지 선택한 경우
                data?.data?.let {
                    imageUris.add(it)
                }
            }
            Log.e("uriList", "$imageUris")
//            val imageUri=result.data?.data

//            imageUri?.let{
            for(i in 0 until imageUris.size){
                var curUri=imageUris[i]
                imageUri.add(curUri)

                imageFile= File(getRealPathFromURI(curUri))
                Log.d("imageFile", "${imageFile}")
//                imagePath = getRealPathFromURI(this,it)

                val now = Date()
                val time: String = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now)

                val renameFile= File(imageFile.parent,"${time}_$i.jpg")
                val requestBody = imageFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                var image=MultipartBody.Part.createFormData("multipartFiles", renameFile.name, requestBody)
                imgFile.add(image)
                binding.tvImageCount.text="(${imgFile.size}/3)"

                Log.e("file name", "${renameFile.name}")

                binding.recyclerviewEditRecord.apply{
                    layoutManager= LinearLayoutManager(this@EditRecordActivity, LinearLayoutManager.HORIZONTAL,false)
                    adapter=RecordImageRecyclerViewAdapter(this@EditRecordActivity, imageUri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarEditRecord)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnBack.setOnClickListener {
            finish()
        }

        initialize()
        datePick()
        watchData()

        binding.linearAddImage.setOnClickListener {
            selectGallery()
        }

        binding.btnEditRecord.setOnClickListener {
            editRecord()
        }
    }

    private fun initialize(){
        recordId=intent.getLongExtra("recordId",0)
        dateValue= intent.getStringExtra("date").toString()
        titleValue=intent.getStringExtra("title").toString()
        experienceValue=intent.getStringExtra("experience").toString()
        conceptValue=intent.getStringExtra("concept").toString()
        resultValue=intent.getStringExtra("result").toString()
        contentValue=intent.getStringExtra("content").toString()
        imageString= intent.getStringArrayListExtra("imgUrls") as ArrayList<String>
        urlValue=intent.getStringExtra("url").toString()

        binding.tvDateRecord.text=dateValue
        binding.etTitleRecord.setText(titleValue)
        binding.etExperienceRecord.setText(experienceValue)
        binding.etSolutionRecord.setText(conceptValue)
        binding.etResultRecord.setText(resultValue)
        binding.etContent.setText(contentValue)
        binding.etUrl.setText(urlValue)

        binding.tvImageCount.text="(${imageString.size}/3)"

        binding.recyclerviewEditRecord.apply{
            layoutManager= LinearLayoutManager(this@EditRecordActivity, LinearLayoutManager.HORIZONTAL,false)
            adapter=RecordEditImageRecyclerViewAdapter(this@EditRecordActivity, imageString!!)
        }
    }

    fun updateImageList(pos:Int){
        imageString.removeAt(pos)
        binding.recyclerviewEditRecord.adapter?.notifyDataSetChanged()
        var count=binding.recyclerviewEditRecord.adapter?.itemCount
        binding.tvImageCount.text="(${count}/3)"
        Log.e("updateImageList", "${count}")
    }

    private fun editRecord(){
        if(check){
            contentValue=binding.etContent.text.toString()
            urlValue=binding.etUrl.text.toString()
//            if(binding.ivDeleteImage.visibility==View.GONE){
//                val emptyRequestBody = "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
//                var image=MultipartBody.Part.createFormData("img", "", emptyRequestBody)
//                imgFile= arrayListOf(image)
//                Log.e("Image", "empty")
//            }

            RecordService.retrofitEditRecord(RecordRequest(recordId,titleValue,dateValue,experienceValue,conceptValue,resultValue,contentValue,urlValue)).enqueue(object:Callback<SimpleResponse>{
                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e("log", response.toString())
                        Log.e("log", response.body().toString())

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
    }

    private fun datePick(){
        binding.linearCalendarStart.setOnClickListener {
            dialog= CalendarDialog(this)
            dialog.show()
            dialog.setOnClickListener(object: CalendarDialog.OnDialogClickListener{
                override fun onClicked(date: String) {
                    dateValue=date
                    binding.tvDateRecord.text=date
                    binding.tvDateRecord.setTextColor(Color.parseColor("#262626"))
                }
            })
        }
    }

    private fun watchData(){
        binding.etTitleRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                titleValue = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etExperienceRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                experienceValue = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etSolutionRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                conceptValue = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etResultRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                resultValue = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkValuesAndChangeButtonColor() {
        if (titleValue.isNotEmpty() && dateValue!="yyyy-mm-dd" && experienceValue.isNotEmpty() && conceptValue.isNotEmpty() && resultValue.isNotEmpty()) {
            binding.btnEditRecord.setBackgroundColor(Color.parseColor("#845EF1"))
            check=true
        } else {
            binding.btnEditRecord.setBackgroundColor(Color.parseColor("#4D845EF1"))
            check=false
        }
    }

    fun getRealPathFromURI(uri: Uri): String?{
        val buildName= Build.MANUFACTURER
        if(buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex=0
        val proj=arrayOf(MediaStore.Images.Media.DATA)
        val cursor=contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result=cursor.getString(columnIndex)
        cursor.close()
        return result
    }
    //
    private fun selectGallery(){
        val writePermission= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if(writePermission== PackageManager.PERMISSION_DENIED || readPermission== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), REQ_GALLERY)
        }else{
            val intent= Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*",
            )
//            startActivityForResult(intent, REQ_GALLERY)
            imageResult.launch(intent)
        }
    }
}