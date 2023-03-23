package com.example.buildupfrontend.record

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityWriteRecordBinding

class WriteRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWriteRecordBinding
    private lateinit var dialog: CalendarDialog

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
}