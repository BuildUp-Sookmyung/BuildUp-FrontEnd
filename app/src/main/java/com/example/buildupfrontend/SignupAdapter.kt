package com.example.buildupfrontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView

class SignupAdapter(val context: Context, val signupQuestionList: ArrayList<SignupQuestion>) : BaseAdapter() // BaseAdapter를 상속받음
{
    override fun getCount(): Int {
        return signupQuestionList.size // UserList 데이터의 개수
    }

    override fun getItem(position: Int): Any {
        return signupQuestionList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_item_user, null)
        val tv_title  = view.findViewById<TextView>(R.id.tv_title)
        val tv_title2 = view.findViewById<TextView>(R.id.tv_title2)

        val user = signupQuestionList[position]

        tv_title.text = user.title
        tv_title2.text = user.title

        return view

    }
}