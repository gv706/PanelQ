package com.application.panelq.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.application.panelq.fragments.DashboardFragment
import com.application.panelq.fragments.MyPanelsFragment
import com.application.panelq.fragments.ProfileFragment
import com.application.panelq.fragments.QuestionsHomeFragment
import com.application.panelq.R
import com.application.panelq.StoreSharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var toolbar: Toolbar
    lateinit var bottomNavigation:BottomNavigationView
    private lateinit var db: DatabaseReference
    private lateinit var panelReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        toolbar=findViewById(R.id.toolbar)
        bottomNavigation=findViewById(R.id.bottom_navigation)
        setSupportActionBar(toolbar)
        db = FirebaseDatabase.getInstance().getReference("PanelQ")

        openHome()
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,QuestionsHomeFragment(this)).commit()
                    true
                }
                /*R.id.subscriptions -> {

                    supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,SubscriptionsFragment()).commit()
                    true
                }*/
                R.id.dashboard -> {

                    supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,DashboardFragment(this)).commit()
                    true
                }
                /*R.id.library -> {

                    supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,LibraryFragment()).commit()
                    true
                }*/
                else -> false
            }
        }
    }

    private fun openHome() {
        supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,QuestionsHomeFragment(this)).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout ->{
                firebaseAuth.signOut()
                StoreSharedPreferences(true,this@MainActivity,false,null,null,null,null,null,null)
                startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            }

            R.id.myProfile -> {
                supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,ProfileFragment()).commit()
            }
            R.id.myPanels -> {
                supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout, MyPanelsFragment(this)
                ).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.userFrameLayout)
        if (currentFragment is QuestionsHomeFragment)
        {
            ActivityCompat.finishAffinity(this@MainActivity)
        }
        else
        {
            if(bottomNavigation.selectedItemId == R.id.home)
            {
                supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,QuestionsHomeFragment(this)).commit()
            }
            else
            {
                bottomNavigation.selectedItemId = R.id.home
                supportFragmentManager.beginTransaction().replace(R.id.userFrameLayout,QuestionsHomeFragment(this)).commit()
            }
        }
    }
}