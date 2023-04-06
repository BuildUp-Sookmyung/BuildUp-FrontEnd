package com.example.buildupfrontend.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityShowImagesBinding

class ShowImagesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShowImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarShowImages)
        val toolbar = supportActionBar!!
        toolbar.setDisplayShowTitleEnabled(false)

        binding.btnClose.setOnClickListener {
            finish()
        }

        var imageList: ArrayList<String> = intent.getStringArrayListExtra("imageList") as ArrayList<String>
        var page=intent.getIntExtra("page",0)

        binding.tvCurrentImage.text="${page+1}/${imageList.size}"

        var viewpager=binding.viewpagerShowImages

        viewpager.adapter=ShowImagesViewpagerAdapter(this,this,imageList,page)
        viewpager.orientation= ViewPager2.ORIENTATION_HORIZONTAL

        viewpager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("page: ", "$page")
                binding.tvCurrentImage.text="${position+1}/${imageList.size}"
            }
        })
    }

    fun setViewpagerPage(pos: Int){
        binding.viewpagerShowImages.setCurrentItem(pos)
    }
}