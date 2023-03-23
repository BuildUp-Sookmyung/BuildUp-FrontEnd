package com.example.buildupfrontend.record

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityDetailRecordBinding

class DetailRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecordBinding

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
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}