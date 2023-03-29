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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.buildupfrontend.GlobalApplication
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityEditActivityBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class EditActivityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditActivityBinding
    private lateinit var categoryIdList: ArrayList<Int>
    private lateinit var dialog: CalendarDialog
    private lateinit var categoryText: String
    private var categoryPos: Int=0
    private var categoryValue: String = ""
    private var activityValue: String = ""
    private var startDateValue: String = "yyyy-mm-dd"
    private var endDateValue: String = "yyyy-mm-dd"
    private lateinit var imageFile: File
    private lateinit var imgFile: MultipartBody.Part
    private lateinit var imagePath: String
    private lateinit var date: LocalDate
    private var REQ_GALLERY=1
    private var check=false

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
                val requestBody = imageFile?.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                imgFile= MultipartBody.Part.createFormData("img", renameFile.name, requestBody)

                Log.e("file name", "${renameFile.name}")
                Log.d("tag", "imagePath: ${imagePath}")
                Log.e("tag", "imageUri: ${imageUri}")

                Glide.with(this)
                    .load(imageUri)
                    .fitCenter()
                    .apply(RequestOptions().override(500,500))
                    .into(binding.ivActivity)

                binding.linearImageNull.visibility=View.GONE
                binding.ivActivity.visibility=View.VISIBLE
                binding.ivDeleteImage.visibility=View.VISIBLE
//                binding.recyclerviewWritediary[pos].findViewById<ImageView>(R.id.imageview_addpicture).adjustViewBounds=true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarEditActivity)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnBack.setOnClickListener {
            finish()
        }

        var activityId=intent.getLongExtra("activityId",0)
        categoryValue= intent.getStringExtra("categoryName").toString()
        activityValue=intent.getStringExtra("activityName").toString()
        var hostName=intent.getStringExtra("hostName")
        var roleName=intent.getStringExtra("roleName")
        startDateValue=intent.getStringExtra("startDate").toString()
        endDateValue=intent.getStringExtra("endDate").toString()
        var urlName=intent.getStringExtra("urlName")
        var activityImg=intent.getStringExtra("activityimg")


        binding.etActivityName.setText(activityValue)
        binding.etHost.setText(hostName)
        binding.etRole.setText(roleName)
        binding.tvCalendarStart.text=startDateValue
        binding.tvCalendarEnd.text=endDateValue
        binding.etUrl.setText(urlName)

        if(activityImg==null){
            binding.linearImageNull.visibility= View.VISIBLE
            binding.ivActivity.visibility= View.GONE
            binding.ivDeleteImage.visibility= View.GONE
        }
        else {
            Glide.with(this@EditActivityActivity)
                .load(activityImg)
                .fitCenter()
                .placeholder(R.drawable.ic_add_image) // 이미지 로딩 시작하기 전 표시할 이미지
                .error(R.drawable.ic_add_image) // 로딩 에러 발생 시 표시할 이미지
                .fallback(R.drawable.ic_add_image) // 로드할 url 이 비어있을(null 등) 경우 표시할 이미지
                .apply(RequestOptions().override(500, 500))
                .into(binding.ivActivity)

            binding.linearImageNull.visibility= View.GONE
            binding.ivActivity.visibility= View.VISIBLE
            binding.ivDeleteImage.visibility= View.VISIBLE
        }

        categoryIdList= GlobalApplication.prefs.getIntegerList("categoryIdList",0)
        val categoryList:ArrayList<String> = GlobalApplication.prefs.getStringList("categoryList",0)
        var categoryPos=categoryList.indexOf(categoryValue)

        if (categoryList != null) {
            categoryList.add("카테고리를 선택해주세요")
        }
        val categoryAdapter=object: ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
            categoryList!!
        ){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

                val v = super.getView(position, convertView, parent)

                if (position == count) {
                    //마지막 포지션의 textView 를 힌트 용으로 사용합니다.
                    (v.findViewById<View>(android.R.id.text1) as TextView).text = ""
                    //아이템의 마지막 값을 불러와 hint로 추가해 줍니다.
                    (v.findViewById<View>(android.R.id.text1) as TextView).hint = getItem(count)
                }

                return v
            }

            override fun getCount(): Int {
                //마지막 아이템은 힌트용으로만 사용하기 때문에 getCount에 1을 빼줍니다.
                return super.getCount() - 1
            }
        }

        binding.spinnerCategory.adapter=categoryAdapter
        binding.spinnerCategory.setSelection(categoryPos)

        binding.linearAddImage.setOnClickListener {
            selectGallery()
        }
        datePick()
        watchData()

        binding.ivDeleteImage.setOnClickListener {
            binding.linearImageNull.visibility=View.VISIBLE
            binding.ivActivity.visibility=View.GONE
            binding.ivDeleteImage.visibility=View.GONE
        }
    }

    private fun datePick(){
        binding.linearCalendarStart.setOnClickListener {
            dialog= CalendarDialog(this)
            dialog.show()
            dialog.setOnClickListener(object: CalendarDialog.OnDialogClickListener{
                override fun onClicked(date: String) {
                    binding.tvCalendarStart.text=date
                    binding.tvCalendarStart.setTextColor(Color.parseColor("#262626"))
                    startDateValue=date
                    checkValuesAndChangeButtonColor()
                }
            })
        }

        binding.linearCalendarEnd.setOnClickListener {
            dialog= CalendarDialog(this)
            dialog.show()
            dialog.setOnClickListener(object: CalendarDialog.OnDialogClickListener{
                override fun onClicked(date: String) {
                    binding.tvCalendarEnd.text=date
                    binding.tvCalendarEnd.setTextColor(Color.parseColor("#262626"))
                    endDateValue=date
                    checkValuesAndChangeButtonColor()
                }
            })
        }
    }

    private fun watchData(){
        binding.spinnerCategory.onItemSelectedListener=object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryText=binding.spinnerCategory.selectedItem.toString()
                categoryValue=parent?.getItemAtPosition(position).toString()
                categoryPos=position
                checkValuesAndChangeButtonColor()
                Log.e("categoryText", "$categoryText")
                Log.e("categoryId", "${position+1}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.etActivityName.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                activityValue = s.toString()
                checkValuesAndChangeButtonColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkValuesAndChangeButtonColor() {
        if (activityValue.isNotEmpty() && startDateValue!="yyyy-mm-dd" && endDateValue!="yyyy-mm-dd") {
            binding.btnEditActivity.setBackgroundColor(Color.parseColor("#845EF1"))
            check=true
        } else {
            binding.btnEditActivity.setBackgroundColor(Color.parseColor("#4D845EF1"))
            check=false
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