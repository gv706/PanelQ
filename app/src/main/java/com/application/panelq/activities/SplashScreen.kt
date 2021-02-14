package com.application.panelq.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.panelq.CurrentUserDetails
import com.application.panelq.R

class SplashScreen : AppCompatActivity() {
    lateinit var sharedPreferences :SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(1024,1024)
        setContentView(R.layout.activity_splash_screen)
        sharedPreferences = getSharedPreferences("PanelQ", Context.MODE_PRIVATE)

        Thread{
            Thread.sleep(3000)
            if(!sharedPreferences.getBoolean("isloggedin",false)) {
                startActivity(Intent(this@SplashScreen,LoginActivity::class.java))
            }
            else {
                CurrentUserDetails.userId = sharedPreferences.getString("userid",null)!!
                CurrentUserDetails.userName = sharedPreferences.getString("name",null)!!
                CurrentUserDetails.userEmail = sharedPreferences.getString("email",null)!!
                CurrentUserDetails.userPhone = sharedPreferences.getString("phone",null)!!
                CurrentUserDetails.userGender = sharedPreferences.getString("gender",null)!!
                startActivity(Intent(this@SplashScreen,MainActivity::class.java))

            }
        }.start()
    }

    override fun onPause() {
        overridePendingTransition(0,0)
        super.onPause()
    }
}