package com.application.panelq.adapters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.application.panelq.Fragments.McqFragment
import com.application.panelq.Fragments.QuestionFragment
import com.application.panelq.Fragments.TfFragment


class ViewPagerAdapter(var con:Context,var fm:FragmentManager,var totalTabs: Int,var panelId:String): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{
                val questionFragment=QuestionFragment(con)
                val bundle=Bundle()
                bundle.putString("panelid",panelId)
                questionFragment.arguments=bundle
                questionFragment
            }
            1->{
                val tfFragment=TfFragment()
                tfFragment
            }
            else->{
                val mcqFragment=McqFragment()
                mcqFragment
            }

        }
    }


    override fun getCount(): Int {
         return totalTabs
    }

}