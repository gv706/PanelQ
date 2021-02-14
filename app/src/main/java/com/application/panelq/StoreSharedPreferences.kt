package com.application.panelq

import android.content.Context
import android.content.SharedPreferences

class StoreSharedPreferences(logout:Boolean,context: Context,isloggedin:Boolean,userid:String?,name:String?,gender:String?,countryCode:String?,phone:String?,email:String?) {
    var sharedPreferences:SharedPreferences = context.getSharedPreferences("PanelQ",Context.MODE_PRIVATE)
    init {
        sharedPreferences.edit().putBoolean("isloggedin",isloggedin).apply()
        sharedPreferences.edit().putString("userid",userid).apply()
        sharedPreferences.edit().putString("name",name).apply()
        sharedPreferences.edit().putString("gender",gender).apply()
        sharedPreferences.edit().putString("countryCode",countryCode).apply()
        sharedPreferences.edit().putString("phone",phone).apply()
        sharedPreferences.edit().putString("email",email).apply()
        if(!logout)
        {
           CurrentUserDetails.userId =userid!!
            CurrentUserDetails.userName=name!!
            CurrentUserDetails.userGender=gender!!
            CurrentUserDetails.userCountryCode = countryCode!!
            CurrentUserDetails.userPhone = phone!!
            CurrentUserDetails.userEmail = email!!
        }

    }

}