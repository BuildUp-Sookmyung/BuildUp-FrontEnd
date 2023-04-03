package com.example.buildupfrontend.record

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.databinding.ActivityWriteRecordBinding
import com.example.buildupfrontend.retrofit.Client.RecordService
import com.example.buildupfrontend.retrofit.Response.SimpleResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class WriteRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteRecordBinding
    private var activityId:Long=0
    private lateinit var dialog: CalendarDialog
    private lateinit var imageFile: File
    private var imageUri= arrayListOf<Uri>()
    private var imgFile = arrayListOf<MultipartBody.Part>()
    private lateinit var imagePath: String
    private var REQ_GALLERY=1
    private var title=""
    private var dateValue="yyyy-mm-dd"
    private var experience=""
    private var solution=""
    private var result=""
    private var check=false

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

                binding.recyclerviewWriteRecord.apply{
                    layoutManager= LinearLayoutManager(this@WriteRecordActivity, LinearLayoutManager.HORIZONTAL,false)
                    adapter=RecordImageRecyclerViewAdapter(this@WriteRecordActivity, imageUri)
                }
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQ_GALLERY && resultCode == Activity.RESULT_OK) {
//            val imageUris = arrayListOf<Uri>()
//            if (data?.clipData != null) {
//                // 다중 이미지 선택한 경우
//                val clipData = data.clipData
//                val count = minOf(clipData?.itemCount!!, 3) // 최대 3개까지만 선택
//                for (i in 0 until count) {
//                    imageUris.add(clipData.getItemAt(i).uri)
//                }
//            } else {
//                // 단일 이미지 선택한 경우
//                data?.data?.let {
//                    imageUris.add(it)
//                }
//            }
//            Log.e("uriList", "$imageUris")
//            handleSelectedImages(imageUris)
//        }
//    }

    private fun handleSelectedImages(uriList: ArrayList<Uri>){
        for(i in 0 until uriList.size){
            var curUri=uriList[i]
            imageFile= File(getRealPathFromURI(curUri))
            Log.d("imageFile", "${imageFile}")
//            imagePath = getRealPathFromURI(curUri)

            val now = Date()
            val time: String = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now)

            val renameFile= File(imageFile.parent,"${time}_$i.jpg")
            val requestBody = imageFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            var image=MultipartBody.Part.createFormData("multipartFiles", renameFile.name, requestBody)
            imgFile.add(image)
            binding.tvImageCount.text="(${imgFile.size}/3)"

            Log.e("file name", "${renameFile.name}")

//            Glide.with(this)
//                .load(curUri)
//                .fitCenter()
//                .apply(RequestOptions().override(500,500))
//                .into(binding.recyclerviewWriteRecord[i].findViewById(R.id.iv_write_record))

//            binding.recyclerviewWriteRecord[i].findViewById<ImageView>(R.id.iv_delete_image).visibility=View.VISIBLE
//            binding.ivActivity.visibility=View.VISIBLE
//            binding.ivDeleteImage.visibility=View.VISIBLE
        }

        binding.recyclerviewWriteRecord.apply{
            layoutManager= LinearLayoutManager(this@WriteRecordActivity, LinearLayoutManager.HORIZONTAL,false)
            adapter=RecordImageRecyclerViewAdapter(this@WriteRecordActivity, uriList)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWriteRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddRecord)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnBack.setOnClickListener {
            finish()
        }

        activityId=intent.getLongExtra("activityId",0)

        datePick()
        watchData()

        binding.linearAddImage.setOnClickListener {
            if(imgFile.size==3)
                Toast.makeText(this,"사진 삭제 후 추가해주세요.",Toast.LENGTH_LONG).show()
            else {
                Toast.makeText(this, "사진은 3개까지만 선택가능합니다.", Toast.LENGTH_LONG).show()
                selectGallery()
            }
        }

        binding.btnWriteRecord.setOnClickListener {
            writeRecord()
        }
    }

    fun updateImageList(pos:Int){
        imgFile.removeAt(pos)
        binding.recyclerviewWriteRecord.adapter?.notifyDataSetChanged()
        var count=binding.recyclerviewWriteRecord.adapter?.itemCount
        binding.tvImageCount.text="(${count}/3)"
        Log.e("updateImageList", "${count}")
    }

    private fun writeRecord(){
        if(check){
            var content=binding.etExtra.text.toString()
            var urlName=binding.etUrl.text.toString()

//            if(binding.ivDeleteImage.visibility==View.GONE){
//                val emptyRequestBody = "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
//                var image=MultipartBody.Part.createFormData("img", "", emptyRequestBody)
//                imgFile= arrayListOf(image)
//                Log.e("Image", "empty")
//            }

            val jsonObject= JSONObject("{\"activityId\":\"${activityId}\", \"recordTitle\":\"${title}\",\"date\":\"${dateValue}\",\"experienceName\":\"${experience}\", \"conceptName\":\"${solution}\", \"resultName\":\"${result}\", \"content\":\"${content}\", \"urlName\":\"${urlName}\"}")
            val mediaType = "application/json".toMediaType()
            val jsonBody=jsonObject.toString().toRequestBody(mediaType)
            Log.e("activity_data", "$jsonBody $imgFile")

            RecordService.retrofitPostRecord(jsonBody, imgFile).enqueue(object:
                Callback<SimpleResponse>{
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
                title = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etExperienceRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                experience = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etSolutionRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                solution = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.etResultRecord.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                result = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkValuesAndChangeButtonColor() {
        if (title.isNotEmpty() && dateValue!="yyyy-mm-dd" && experience.isNotEmpty() && solution.isNotEmpty() && result.isNotEmpty()) {
            binding.btnWriteRecord.setBackgroundColor(Color.parseColor("#845EF1"))
            check=true
        } else {
            binding.btnWriteRecord.setBackgroundColor(Color.parseColor("#4D845EF1"))
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

//    fun getPathFromInputStreamUri(context: Context, uri: Uri): String? {
//        var filePath: String? = null
//        uri.authority?.let {
//            try {
//                context.contentResolver.openInputStream(uri).use {
//                    val photoFile: File? = createTemporalFileFrom(it)
//                    filePath = photoFile?.path
//                }
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return filePath
//    }
//
//    @Throws(IOException::class)
//    private fun createTemporalFileFrom(inputStream: InputStream?): File? {
//        var targetFile: File? = null
//        return if (inputStream == null) targetFile
//        else {
//            var read: Int
//            val buffer = ByteArray(8 * 1024)
//            targetFile = createTemporalFile()
//            FileOutputStream(targetFile).use { out ->
//                while (inputStream.read(buffer).also { read = it } != -1) {
//                    out.write(buffer, 0, read)
//                }
//                out.flush()
//            }
//            targetFile
//        }
//    }
//
//    private fun createTemporalFile(): File = File(getDefaultDirectory(), "tempPicture.jpg")
//
//    private fun getDefaultDirectory(): String= Paths.get("").toAbsolutePath().toString()

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