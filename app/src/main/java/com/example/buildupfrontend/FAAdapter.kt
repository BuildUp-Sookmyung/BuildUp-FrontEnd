package com.example.buildupfrontend

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.buildupfrontend.FragmentId
import com.example.buildupfrontend.FragmentPw
public class FAAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {
    private val fragmentArrayList:ArrayList<Fragment> = ArrayList();
    private val fragmentTitle:ArrayList<String> = ArrayList();

    override fun getItem(position: Int): Fragment {
        return fragmentArrayList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

    override fun getCount(): Int {
        return fragmentArrayList.size
    }

    fun addFragment(fragment:Fragment, title:String) {
        fragmentArrayList.add(fragment)
        fragmentTitle.add(title)
    }

}

