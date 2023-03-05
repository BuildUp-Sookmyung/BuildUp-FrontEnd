
package com.example.buildupfrontend.CardViewActivity

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.buildupfrontend.R
import com.example.buildupfrontend.databinding.ActivityCardViewBinding


class CardViewActivity : AppCompatActivity() {
    private var mBinding: ActivityCardViewBinding? = null
    private val binding get() = mBinding!!

    var labels = arrayOf("1","2","3","4")
    var blocks = intArrayOf(R.drawable.img_number_block_01_red,
        R.drawable.img_number_block_02_orange,
        R.drawable.img_number_block_03_yellow,
        R.drawable.img_number_block_04_green,
        R.drawable.img_number_block_05_deepgreen,
        R.drawable.img_number_block_06_sky)
    var titles = arrayOf("첫 번째 멘토링", "두 번째 멘토링", "세 번째 멘토링", "네 번째 멘토링")
    var dates = arrayOf("2023-01-27", "2023-01-26", "2023-01-25", "2023-01-24")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCardViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pbActivity.progress = 25

        val layoutManager = LinearLayoutManager(this)
        binding.rvCardRecord.layoutManager = layoutManager
        binding.rvCardRecord.adapter = CardDefaultAdapter(this, labels, blocks, titles, dates)

        setDefaultView()
    }

    fun onClick(v: View) {
        when(v.id) {
            R.id.btn_deleteAll ->{
                // 활동 전체 삭제
                val mDialogView = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_delete, null)
                val mAlertDialog = AlertDialog.Builder(this, R.style.DetailDialog)
                    .setView(mDialogView)
                    .show()
                // Dialog button control
                val btnDelete =
                    mDialogView.findViewById<AppCompatButton>(R.id.btn_delete)
                val btnCancel =
                    mDialogView.findViewById<AppCompatButton>(R.id.btn_cancel)
                val btnClose =
                    mDialogView.findViewById<AppCompatButton>(R.id.btn_close)
                btnDelete.setOnClickListener {
                    deleteActivity()
                }
                btnCancel.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                btnClose.setOnClickListener {
                    mAlertDialog.dismiss()
                }
            }
            R.id.tv_deleteCheck ->{
                binding.rvCardRecord.adapter = CardDeleteAdapter(this, labels, blocks, titles, dates)

            }
        }
    }

    private fun deleteActivity() {
        return
        TODO("Delete entire activity")
    }

    private fun setDefaultView() {

    }
}