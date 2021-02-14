package com.application.panelq.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.application.panelq.AccountDetails
import com.application.panelq.R
import com.application.panelq.StoreSharedPreferences
import com.application.panelq.databinding.ActivityLoginBinding
import com.application.panelq.network.ConnectionManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var txtSignUp:TextView
    private lateinit var txtForgotPassword:TextView
    private lateinit var btnLogin:Button
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tieEmail:TextInputEditText
    private lateinit var tiePassword:TextInputEditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var pattern: Pattern
    private lateinit var progress:ProgressBar
    private lateinit var binding:ActivityLoginBinding
    private lateinit var db:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth= FirebaseAuth.getInstance()
        db=FirebaseDatabase.getInstance().getReference("PanelQ")
        pattern=Patterns.EMAIL_ADDRESS
        tilEmail=binding.tilEmail
        tilPassword=binding.tilPassword
        progress=binding.progressHorizontal
        tieEmail=tilEmail.editText as TextInputEditText
        tiePassword=tilPassword.editText as TextInputEditText
        txtForgotPassword=binding.txtForgotPassword
        txtSignUp=binding.txtSignUp
        txtSignUp.setOnClickListener {
            tilEmail.error=null
            tilPassword.error=null
            tieEmail.text=null
            tiePassword.text=null
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
        btnLogin=findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            tilEmail.error=null
            tilPassword.error=null
            val strEmail=tieEmail.text.toString().trim()
            val strPassword=tiePassword.text.toString()
            if (strEmail.isEmpty()){
                tilEmail.error="Please enter the email"
                return@setOnClickListener
            }
            if(!pattern.matcher(strEmail).matches()){
                tilEmail.error="Please provide the valid email"
                return@setOnClickListener
            }
            if(strPassword.isEmpty()){
                tilPassword.error="Please enter the password"
                return@setOnClickListener
            }
            if(strPassword.length<8){
                tilPassword.error="Password should contain atleast 8 characters"
                return@setOnClickListener
            }
            val connectionManager = ConnectionManager()
            if (connectionManager.isNetworkAvailable(this@LoginActivity)){

                putEverythingEnabled(false)

                firebaseAuth.signInWithEmailAndPassword(strEmail,strPassword).addOnCompleteListener {

                    if(it.isSuccessful){
                        retrieveData(strEmail)
                    }
                    else{
                        putEverythingEnabled(true)
                        StoreSharedPreferences(true,this@LoginActivity,false,null,null,null,null,null,null)
                        Toast.makeText(applicationContext,it.exception?.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Snackbar.make(btnLogin,"Check your network connection", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun putEverythingEnabled(b: Boolean) {
        tilEmail.isEnabled=b
        tilPassword.isEnabled=b
        btnLogin.isEnabled=b
        txtSignUp.isEnabled=b
        txtForgotPassword.isEnabled=b
        if(b){
            btnLogin.text=getString(R.string.login)
            progress.visibility= View.INVISIBLE
        }
        else{
            btnLogin.text=getString(R.string.signingin)
            progress.visibility= View.VISIBLE
        }
    }

    private fun retrieveData(email:String) {
        db.child("Users").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val accountDetails: AccountDetails? = child.getValue(AccountDetails::class.java)
                    putEverythingEnabled(true)
                    StoreSharedPreferences(false,this@LoginActivity,true,child?.key.toString(),accountDetails?.name,accountDetails?.gender,accountDetails?.countryCode,accountDetails?.phone,accountDetails?.email)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                putEverythingEnabled(true)
                StoreSharedPreferences(true,this@LoginActivity,false,null,null,null,null,null,null)
                Toast.makeText(applicationContext, error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this@LoginActivity)
    }
}