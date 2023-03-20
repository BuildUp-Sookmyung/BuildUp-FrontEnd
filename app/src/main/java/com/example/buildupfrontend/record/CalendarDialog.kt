package com.example.buildupfrontend.record

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup

import com.example.buildupfrontend.databinding.CalendarDialogBinding
import java.util.*


class CalendarDialog(context: Context): Dialog(context) {
    private lateinit var binding: CalendarDialogBinding
    private lateinit var onClickListener: CalendarDialog.OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    interface OnDialogClickListener
    {
        fun onClicked(date: String)
    }

    init {
        binding = CalendarDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.imgCalendarClose.setOnClickListener {
            dismiss()
        }

        datePick()
    }

    private fun datePick(){
        var today=Calendar.getInstance()
        var year=today.get(Calendar.YEAR)
        var month= today.get(Calendar.MONTH)+1
        var day=today.get(Calendar.DAY_OF_MONTH)

        var mon: Int=month
        var yea: Int=year
        var da: Int=day

        binding.datepicker.init(year,month-1,day){
                view, year, month, day ->

            mon = month + 1
            da=day
            yea=year
        }

        binding.calendarDialogComplete.setOnClickListener {
            var m=mon.toString()
            var d=da.toString()
            if(mon<10){
                m="0${mon}"
            }
            if(da<10)
                d="0${da}"

            onClickListener.onClicked("$yea-$m-$d")
            dismiss()
        }
    }
}