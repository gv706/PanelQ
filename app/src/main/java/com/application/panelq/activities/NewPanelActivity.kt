package com.application.panelq.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.application.panelq.CurrentUserDetails
import com.application.panelq.PanelInfo
import com.application.panelq.R
import com.application.panelq.StorePanelAccountPreferences
import com.application.panelq.databinding.ActivityNewPanelBinding
import com.application.panelq.network.ConnectionManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class NewPanelActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNewPanelBinding
    private lateinit var toolbar : Toolbar
    private lateinit var panelName: TextInputLayout
    private lateinit var panelDescription: TextInputLayout
    private lateinit var panelProfile : CircleImageView
    private lateinit var createPanelButton : Button
    private lateinit var chooseProfileButton : ImageView
    private lateinit var progressBar: ProgressBar
    private  var filepath : Uri? = null
    private lateinit var db: DatabaseReference
    lateinit var bitmap: Bitmap
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = binding.createPanelAccountToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title="Create Panel"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        panelName = binding.panelName
        panelDescription = binding.panelDescription
        panelProfile = binding.choosenProfileImage
        createPanelButton = binding.createPanelButton
        chooseProfileButton = binding.chooseProfileButton
        progressBar=binding.progressHorizontal
        db = FirebaseDatabase.getInstance().getReference("PanelQ")
        chooseProfileButton.setOnClickListener {
            startFileChooser()
        }
        createPanelButton.setOnClickListener {
            panelName.error = null
            panelDescription.error = null
            val strPanelName = panelName.editText!!.text.toString().trim()
            val strPanelDescription = panelDescription.editText!!.text.toString().trim()
            if (strPanelName.isEmpty()) {
                panelName.error = "Please enter the name"
                panelName.requestFocus()
                return@setOnClickListener
            }
            if (strPanelName.length < 2) {
                panelName.error = "atleast 2 characters"
                panelName.requestFocus()
                return@setOnClickListener
            }
            if (strPanelDescription.isEmpty()) {
                panelDescription.error = "Please enter the name"
                panelDescription.requestFocus()
                return@setOnClickListener
            }
            if (strPanelDescription.length < 2) {
                panelDescription.error = "atleast 2 characters"
                panelDescription.requestFocus()
                return@setOnClickListener
            }
            if(filepath == null)
            {
                Toast.makeText(applicationContext,"Please select an profile image",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            firebaseAuth= FirebaseAuth.getInstance()
            db = FirebaseDatabase.getInstance().getReference("PanelQ")
            val connectionManager = ConnectionManager()
            if (connectionManager.isNetworkAvailable(this@NewPanelActivity)) {
                putEverythingEnabled(false)
                val imageRef: StorageReference = FirebaseStorage.getInstance().reference.child(CurrentUserDetails.userId).child("panels").child(strPanelName).child("profile.png")
                imageRef.putFile(filepath!!).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    imageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        val downloadUri = task.result
                        val key=db.child("Users").child(firebaseAuth.uid!!).child("Panels").push().key.toString()
                        val panelAccountDetails= PanelInfo(key,strPanelName,strPanelDescription,downloadUri.toString(), Date().toString())
                        db.child("Users").child(firebaseAuth.uid!!).child("Panels").child(key).child("Panel Details").setValue(panelAccountDetails).addOnCompleteListener {
                                task ->
                            if(task.isSuccessful){
                                StorePanelAccountPreferences(false,this@NewPanelActivity,strPanelName,strPanelDescription,downloadUri.toString())
                                Toast.makeText(applicationContext,"Panel created successfully!",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@NewPanelActivity,MainActivity::class.java))
                            }
                            else
                            {
                                StorePanelAccountPreferences(false,this@NewPanelActivity,null,null,null)
                                Toast.makeText(applicationContext,task.exception?.message,Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else
                    {

                        Toast.makeText(applicationContext,task.exception?.message,Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }
    private fun putEverythingEnabled(enable:Boolean) {
        panelName.isEnabled = enable
        panelDescription.isEnabled = enable
        panelProfile.isEnabled = enable
        chooseProfileButton.isEnabled = enable
        createPanelButton.isEnabled =enable
        if(enable) {
            progressBar.visibility = View.INVISIBLE
            createPanelButton.text = getString(R.string.create_panel)
        }
        else{
            progressBar.visibility = View.VISIBLE
            createPanelButton.text = getString(R.string.creating_panel)
        }
    }
    private fun startFileChooser() {
        val i = Intent()
        i.setType("image/*")
        i.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(i,"Choose Picture"),111)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111 && resultCode == Activity.RESULT_OK && data != null)
        {
            filepath = data.data!!
            Picasso.with(this).load(filepath).into(panelProfile)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}