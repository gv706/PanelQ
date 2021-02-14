package com.application.panelq.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.application.panelq.R
import com.application.panelq.adapters.ViewPagerAdapter
import com.application.panelq.databinding.ActivitySelectedPanelBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.qna_home_single_row.*

class SelectedPanelActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySelectedPanelBinding
    private lateinit var toolbar : Toolbar
    private lateinit var viewPager : ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var fabNewPost: FloatingActionButton
    private lateinit var imgPanelProfile : CircleImageView
    private lateinit var viewPagerAdapter:ViewPagerAdapter
    var panelName: String?=""
    var panelProfile: String?=""
    var panelDesc: String?=""
    var panelId: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectedPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = binding.createPanelAccountToolbar
        imgPanelProfile = binding.imgPanelProfile
        setSupportActionBar(toolbar)
        panelId=intent.getStringExtra("panelid")
        panelDesc=intent.getStringExtra("paneldescription")
        panelProfile = intent.getStringExtra("panelprofile")
        panelName = intent.getStringExtra("panelname")
        //Toast.makeText(applicationContext,panelName, Toast.LENGTH_LONG).show()
        txtPanelName.text = panelName
        Picasso.with(this).load(panelProfile).into(imgPanelProfile)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        viewPagerAdapter =  ViewPagerAdapter(this,supportFragmentManager,tabLayout.tabCount,panelId!!)
        viewPager.adapter=viewPagerAdapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

      fabNewPost =  binding.btnCreatePost
        fabNewPost.setOnClickListener {
            createPostForm()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun createPostForm() {

        val options = arrayOf("QnA")
        var selectedItem = 0
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select post")
        builder.setSingleChoiceItems(options, 0)
        {
                dialogInterface: DialogInterface, item: Int ->
            selectedItem = item
        }
        val intentPost = Intent(applicationContext, QnAActivity::class.java)
        intentPost.putExtra("panelid",panelId)
        builder.setPositiveButton("Select") { dialogInterface: DialogInterface, p1: Int ->
            if(options[selectedItem] == "QnA")
            {
                startActivity(intentPost)
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("cancel") { dialogInterface: DialogInterface, p1: Int ->
            dialogInterface.dismiss()
        }
        builder.create()
        builder.show()


    }


}