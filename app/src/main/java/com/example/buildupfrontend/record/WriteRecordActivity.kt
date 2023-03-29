package com.example.buildupfrontend.record

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityWriteRecordBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WriteRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteRecordBinding
    private lateinit var dialog: CalendarDialog
    private lateinit var imageFile: File
    private lateinit var imgFile: MultipartBody.Part
    private lateinit var imagePath: String
    private var REQ_GALLERY=1

    val imageResult=registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result->
        if(result.resultCode==RESULT_OK){
            val imageUri=result.data?.data
            imageUri?.let{
                imageFile= File(getRealPathFromURI(it))
                Log.d("imageFile", "${imageFile}")
                imagePath = getRealPathFromURI(it)

                val now = Date()
                val time: String = SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH).format(now)

                val renameFile= File(imageFile.parent,"${time}.jpg")
                var requestBody = imageFile?.asRequestBody("image/*".toMediaTypeOrNull())
                imgFile=MultipartBody.Part.createFormData("image", renameFile.name, requestBody)

                Log.d("imgFile", "${imgFile}")
//                Log.e("file name", "${renameFile.name}")
//                Log.d("tag", "imagePath: ${imagePath}")
                Log.d("tag", "imageUri: ${imageUri}")

                Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .apply(RequestOptions().override(500,500))
//                    .into(binding.ivActivity)
//
//                binding.linearImageNull.visibility= View.GONE
//                binding.ivActivity.visibility= View.VISIBLE
//                binding.ivDeleteImage.visibility= View.VISIBLE
//                binding.recyclerviewWritediary[pos].findViewById<ImageView>(R.id.imageview_addpicture).adjustViewBounds=true
            }
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

        datePick()
    }

    private fun datePick(){
        binding.linearCalendarStart.setOnClickListener {
            dialog= CalendarDialog(this)
            dialog.show()
            dialog.setOnClickListener(object: CalendarDialog.OnDialogClickListener{
                override fun onClicked(date: String) {
                    binding.tvDateRecord.text=date
                    binding.tvDateRecord.setTextColor(Color.parseColor("#262626"))
                }
            })
        }
    }

    fun getRealPathFromURI(uri: Uri): String{
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
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
            imageResult.launch(intent)
        }
    }
}