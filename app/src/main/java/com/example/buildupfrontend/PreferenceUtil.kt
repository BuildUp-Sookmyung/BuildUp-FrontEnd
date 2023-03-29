package com.example.buildupfrontend

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PreferenceUtil(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key, str).apply()
    }

    fun getIntegerList(key: String, defValue: Int):ArrayList<Int>{
        val listSize=prefs.getInt(key,defValue)
        val list= ArrayList<Int>()
        for(i in 0 until listSize){
            val item=prefs.getInt("$key[$i]",defValue)
            list.add(item)
        }
        return list
    }

    fun setIntegerList(key: String, list: ArrayList<Int>){
        val editor=prefs.edit()
        editor.putInt(key,list.size).apply()

        for((i,item) in list.withIndex()){
            editor.putInt("$key[$i]", item).apply()
        }
    }

    fun getStringList(key: String, defValue: Int):ArrayList<String>{
        val listSize=prefs.getInt(key,0)
        val list= ArrayList<String>()
        for(i in 0 until listSize){
            val item=prefs.getString("$key[$i]","")
            item?.let{
                list.add(it)
            }
        }
        return list
    }

    fun setStringList(key: String, list: ArrayList<String>){
        val editor=prefs.edit()
        editor.putInt(key,list.size).apply()

        for((i,item) in list.withIndex()){
            editor.putString("$key[$i]", item).apply()
        }
    }
}