package com.application.panelq.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.application.panelq.CurrentUserDetails
import com.application.panelq.QnAInfo
import com.application.panelq.QnAMoreInfo
import com.application.panelq.R
import com.application.panelq.databinding.ActivityQnaBinding
import com.application.panelq.network.ConnectionManager
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class QnAActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQnaBinding
    private lateinit var toolbar: Toolbar
    private lateinit var question: TextInputLayout
    private lateinit var answer: TextInputLayout
    private lateinit var createPostButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var db: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    var strPanelId:String?=""
    var strUserId:String=CurrentUserDetails.userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQnaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = binding.createPanelAccountToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create Question"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        strPanelId=intent.getStringExtra("panelid")
        question = binding.question
        answer = binding.answer

        createPostButton = binding.createPostButton

        progressBar = binding.progressHorizontal
        db = FirebaseDatabase.getInstance().getReference("PanelQ")

        createPostButton.setOnClickListener {
            question.error = null
            answer.error = null
            val strQuestion = question.editText!!.text.toString().trim()
            val strAnswer = answer.editText!!.text.toString().trim()
            if (strQuestion.isEmpty()) {
                question.error = "Please enter the question"
                question.requestFocus()
                return@setOnClickListener
            }

            if (strAnswer.isEmpty()) {
                answer.error = "Please enter the answer"
                answer.requestFocus()
                return@setOnClickListener
            }

            firebaseAuth = FirebaseAuth.getInstance()
            db = FirebaseDatabase.getInstance().getReference("PanelQ")
            val connectionManager = ConnectionManager()
            if (connectionManager.isNetworkAvailable(this@QnAActivity)) {
                putEverythingEnabled(false)

                val key=db.child("All Posts").child("QnA").push().key.toString()
                val createdOn= Date().toString()
                val qnaMoreDetails = QnAMoreInfo(
                    strPanelId,
                    strUserId,
                    createdOn,
                    0,
                    strQuestion,
                    strAnswer
                )
                db.child("All Posts").child("QnA").child(key).setValue(qnaMoreDetails).addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        {
                            val qnaDetails=QnAInfo(strQuestion,createdOn)
                            db.child("Users").child(strUserId).child("Panels").child(strPanelId!!)
                                .child("Posts").child("QnA").child(key).setValue(qnaDetails)
                            Toast.makeText(applicationContext,"Post sent",Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else
                        {
                            Toast.makeText(applicationContext,"Error occured",Toast.LENGTH_SHORT).show()
                            putEverythingEnabled(true)
                        }
                    }

            }
            else{
               Toast.makeText(applicationContext,"Check your network connection",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun putEverythingEnabled(enable: Boolean) {
        question.isEnabled = enable
        answer.isEnabled = enable
        createPostButton.isEnabled = enable
        if (enable) {
            progressBar.visibility = View.INVISIBLE
            createPostButton.text = getString(R.string.create_post)
        } else {
            progressBar.visibility = View.VISIBLE
            createPostButton.text = getString(R.string.creating_post)
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