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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityEditOtherRecordBinding
import com.example.buildupfrontend.retrofit.Client.RecordService
import com.example.buildupfrontend.retrofit.Request.RecordRequest
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditOtherRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditOtherRecordBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditOtherRecordBinding.inflate(layoutInflater)
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
            if(imgFile.size==3)
                Toast.makeText(this,"사진 삭제 후 추가해주세요.", Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(this, "사진은 3개까지만 선택가능합니다.", Toast.LENGTH_LONG).show()
                selectGallery()
            }
        }

        binding.btnEditRecord.setOnClickListener {
            Log.e("Edit Record","수정 완료 버튼 클릭")
            if(changeImg)
                editImg()
            editRecord()
        }

    }

    fun checkDeleteImg(){
        changeImg=true
        Log.e("changeImg","$changeImg, 이미지가 삭제됨")
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

//        if(imageString[0]==null)
//            imageString= arrayListOf()

        var stringList= arrayListOf<String>()
        for(i in 0 until imageString.size){
            if(imageString[i]!=null)
                stringList.add(imageString[i])
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                // url을 file로 변환하는 코드 작성
                for (i in 0 until stringList.size) {
                    val now = Date()
                    val time: String =
                        SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now)

                    var file = File(cacheDir, "${time}_$i.jpg")
                    file.createNewFile()
                    var uri = Uri.fromFile(file)

                    val inputStream = URL(stringList[i]).openStream()
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)

                    imageUri.add(uri)

                    val requestBody = file?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    var image =
                        MultipartBody.Part.createFormData("multipartFiles", file.name, requestBody)
                    Log.e("imgFile", "$image")
                    imgFile.add(image)
                }

            }
            withContext(Dispatchers.Main) {
                binding.tvImageCount.text="(${stringList.size}/3)"

                binding.recyclerviewEditRecord.apply {
                    layoutManager = LinearLayoutManager(
                        this@EditOtherRecordActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    adapter = OtherRecordEditImageRecyclerViewAdapter(
                        this@EditOtherRecordActivity,
                        this@EditOtherRecordActivity,
                        imageUri
                    )
                }
            }
        }
        checkValuesAndChangeButtonColor()
    }

    fun updateImageList(pos:Int){
        imgFile.removeAt(pos)

        binding.recyclerviewEditRecord.adapter?.notifyDataSetChanged()
        var count=binding.recyclerviewEditRecord.adapter?.itemCount
        binding.tvImageCount.text="(${count}/3)"
        Log.e("updateImageList", "${count}")
    }

    private fun editRecord(){
        if(check){
            contentValue=binding.etContent.text.toString()
            urlValue=binding.etUrl.text.toString()

            RecordService.retrofitEditRecord(RecordRequest(recordId,titleValue,dateValue,experienceValue,conceptValue,resultValue,contentValue,urlValue)).enqueue(object:
                Callback<SimpleResponse> {
                override fun onResponse(
                    call: Call<SimpleResponse>,
                    response: Response<SimpleResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.e("log", response.toString())
                        Log.e("log", response.body().toString())

                        if(!changeImg) {
                            Toast.makeText(this@EditOtherRecordActivity, "기록이 수정되었습니다.", Toast.LENGTH_LONG).show()
                            finish()
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

                override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                    Log.e("TAG", "실패원인: {$t}")

                }
            })

        }
    }

    private fun editImg(){
        val jsonObject= JSONObject("{\"recordId\":\"${recordId}\"}")
        val mediaType = "application/json".toMediaType()
        val jsonBody=jsonObject.toString().toRequestBody(mediaType)

        RecordService.retrofitEditRecordImg(jsonBody,imgFile).enqueue(object:
            Callback<SimpleResponse> {
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if (response.isSuccessful) {
                    Log.e("log", response.toString())
                    Log.e("log", response.body().toString())

                    Toast.makeText(this@EditOtherRecordActivity, "기록이 수정되었습니다.", Toast.LENGTH_LONG).show()
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

    private fun datePick(){
        binding.linearCalendarStart.setOnClickListener {
            dialog= CalendarDialog(this,dateValue)
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
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.etExperienceRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                experienceValue = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
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
                val count = minOf(clipData?.itemCount!!, 3-imgFile.size) // 최대 3개까지만 선택
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

            for(i in 0 until imageUris.size){
                var curUri=imageUris[i]
                imageUri.add(curUri)

                imageFile= File(getRealPathFromURI(curUri))
                Log.d("imageFile", "${imageFile}")

                val now = Date()
                val time: String = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now)

                val renameFile= File(imageFile.parent,"${time}_$i.jpg")
                val requestBody = imageFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                var image=
                    MultipartBody.Part.createFormData("multipartFiles", renameFile.name, requestBody)
                imgFile.add(image)
                binding.tvImageCount.text="(${imgFile.size}/3)"

                Log.e("file name", "${renameFile.name}")

                binding.recyclerviewEditRecord.apply{
                    layoutManager= LinearLayoutManager(this@EditOtherRecordActivity, LinearLayoutManager.HORIZONTAL,false)
                    adapter=OtherRecordEditImageRecyclerViewAdapter(this@EditOtherRecordActivity,this@EditOtherRecordActivity, imageUri)
                }
            }
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
            val intent= Intent(Intent.ACTION_PICK)
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