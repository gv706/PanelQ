package com.application.panelq


import android.content.Context
import android.content.SharedPreferences

class StorePanelAccountPreferences(logout:Boolean,context: Context,panelname:String?,paneldescription:String?,panelprofile:String?) {
    var panelAccountPreferences:SharedPreferences = context.getSharedPreferences("PanelQ",Context.MODE_PRIVATE)
    init {
        panelAccountPreferences.edit().putString("panelname",panelname).apply()
        panelAccountPreferences.edit().putString("paneldescription",paneldescription).apply()
        panelAccountPreferences.edit().putString("panelprofile",panelprofile).apply()
        if(!logout)
        {
            CurrentUserDetails.panelName = panelname!!
            CurrentUserDetails.panelDescription = paneldescription!!
            CurrentUserDetails.panelProfile = panelprofile!!

        }

    }

}