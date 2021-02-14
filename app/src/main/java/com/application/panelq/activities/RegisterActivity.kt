package com.application.panelq.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.application.panelq.AccountDetails
import com.application.panelq.R
import com.application.panelq.StoreSharedPreferences
import com.application.panelq.databinding.ActivityRegisterBinding
import com.application.panelq.network.ConnectionManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.hbb20.CountryCodePicker
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {
    private lateinit var tilName: TextInputLayout
    private lateinit var tilPhone: TextInputLayout
    private lateinit var countryCodePicker : CountryCodePicker
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var genderGroup: RadioGroup
    private lateinit var genderRadioButton: RadioButton
    private lateinit var btnSignUp: Button
    private lateinit var db: DatabaseReference
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var pattern: Pattern
    private lateinit var progressBar: ProgressBar
    private lateinit var connectionManager: ConnectionManager
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = binding.progressHorizontal
        tilName = binding.tilName
        countryCodePicker = binding.ccp
        tilPhone = binding.tilPhone
        tilEmail = binding.tilEmail
        genderGroup = binding.radioGroupGender
        tilPassword = binding.tilPassword
        tilConfirmPassword = binding.tilConfirmPassword
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create Account"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        firebaseAuth= FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("PanelQ")
        connectionManager = ConnectionManager()
        pattern = Patterns.EMAIL_ADDRESS
        btnSignUp = binding.btnSignup
        btnSignUp.setOnClickListener {
            tilName.error = null
            tilEmail.error = null
            tilPhone.error = null
            tilPassword.error = null
            tilConfirmPassword.error = null
            val strName: String = tilName.editText!!.text.toString().trim()
            val strGender:String
            val strCountryCode : String = countryCodePicker.getFullNumber().toString()
            val strEmail: String = tilEmail.editText!!.text.toString().trim()
            val strPhone = tilPhone.editText!!.text.toString().trim()
            val strPassword: String = tilPassword.editText!!.text.toString()
            val strConfirmPassword: String = tilConfirmPassword.editText!!.text.toString().trim()
            if (strName.isEmpty()) {
                tilName.error = "Please enter the name"
                tilName.requestFocus()
                return@setOnClickListener
            }
            if (strName.length < 2) {
                tilName.error = "atleast 2 characters"
                tilName.requestFocus()
                return@setOnClickListener
            }
            val selectedId: Int = genderGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this@RegisterActivity, "Please select the gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                genderRadioButton = findViewById(selectedId)
                strGender = genderRadioButton.text.toString()
            }
            if (strPhone.isEmpty()) {
                tilPhone.error = "Please enter the Phone"
                tilPhone.requestFocus()
                return@setOnClickListener
            }
            if (strPhone.length != 10) {
                tilPhone.error = "Phone must contain exactly 10 digits"
                tilPhone.requestFocus()
                return@setOnClickListener
            }
            if (strEmail.isEmpty()) {
                tilEmail.error = "Please enter the email"
                tilEmail.requestFocus()
                return@setOnClickListener
            }
            if (!pattern.matcher(strEmail).matches()) {
                tilEmail.error = "Please provide the valid email"
                tilEmail.requestFocus()
                return@setOnClickListener
            }

            if (strPassword.isEmpty()) {
                tilPassword.error = "Please enter the password"
                tilPassword.requestFocus()
                return@setOnClickListener
            }
            if (strPassword.length < 8) {
                tilPassword.error = "Password must contain atleast 8 characters"
                tilPassword.requestFocus()
                return@setOnClickListener
            }
            if (strConfirmPassword.isEmpty()) {
                tilConfirmPassword.error = "Confirm your password"
                tilConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            if (strConfirmPassword != strPassword) {
                tilConfirmPassword.error = "Both passwords should match"
                tilConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            val connectionManager = ConnectionManager()
            if (connectionManager.isNetworkAvailable(this@RegisterActivity)) {
                putEverythingEnabled(false)
                firebaseAuth.createUserWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener {
                    if(it.isSuccessful){
                        val accountDetails= AccountDetails(strName,strGender,strCountryCode,strPhone,strEmail)
                        db.child("Users").child(firebaseAuth.uid!!).setValue(accountDetails).
                        addOnCompleteListener { task ->
                            putEverythingEnabled(true)
                            if(task.isSuccessful){
                                StoreSharedPreferences(false,this@RegisterActivity,true,firebaseAuth.uid,strName,strGender,strCountryCode,strPhone,strEmail)
                                Toast.makeText(applicationContext,"Account created successfully!",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegisterActivity,MainActivity::class.java))
                            }
                            else
                            {
                                StoreSharedPreferences(true,this@RegisterActivity,false,null,null,null,null,null,null)
                                Toast.makeText(applicationContext,task.exception?.message,Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                    else{
                        putEverythingEnabled(true)
                        StoreSharedPreferences(true,this@RegisterActivity,false,null,null , null,null,null,null)
                        Toast.makeText(applicationContext,it.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun putEverythingEnabled(enable:Boolean) {
        tilName.isEnabled = enable
        tilEmail.isEnabled = enable
        tilPassword.isEnabled = enable
        tilConfirmPassword.isEnabled = enable
        btnSignUp.isEnabled =enable
        if(enable) {
            progressBar.visibility = View.INVISIBLE
            btnSignUp.text = getString(R.string.sign_up)
        }
        else{
            progressBar.visibility = View.VISIBLE
            btnSignUp.text = getString(R.string.signingup)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

