package com.application.panelq.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.application.panelq.CurrentUserDetails
import com.application.panelq.databinding.ActivityQuestionPostBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class QuestionPostActivity : AppCompatActivity() {

    private lateinit var toolbar : Toolbar
    private lateinit var db: DatabaseReference
    lateinit var strPanelName:String
    lateinit var strPanelProfile:String
    //lateinit var strPanelDescription:String
    lateinit var strHowManySolved:String
    lateinit var strPostUploaded:String
    lateinit var strAnswer:String
    lateinit var strQuestion:String
    lateinit var postPanelName:TextView
    lateinit var postPanelProfile:CircleImageView
    private lateinit var giveAnswer: TextInputLayout
    private lateinit var giveAnswerEditText :TextInputEditText
    private lateinit var strGivenAnswer : String
    private lateinit var btnSubmitAnswer : Button
    //lateinit var postPanelDescription:TextView
    lateinit var postHowManySolved:TextView
    lateinit var postDateUploaded:TextView
    lateinit var postAnswer:TextView
    lateinit var postQuestion:TextView
    lateinit var strPostId:String
    lateinit var strpanelId:String
    lateinit var strUploaderId:String
    private lateinit var binding:ActivityQuestionPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQuestionPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= FirebaseDatabase.getInstance().getReference("PanelQ")
        toolbar = binding.post
        setSupportActionBar(toolbar)
        supportActionBar?.title="Post"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        strQuestion=intent?.getStringExtra("question")!!
        strAnswer=intent?.getStringExtra("answer")!!
        strpanelId=intent?.getStringExtra("panelid")!!
        strPostId=intent?.getStringExtra("postid")!!
        strUploaderId=intent?.getStringExtra("uploaderid")!!
        strPostUploaded=intent?.getStringExtra("dateuploaded")!!
        strHowManySolved=intent?.getStringExtra("howmanysolved")!!
        strPanelName=intent?.getStringExtra("panelname")!!
        strPanelProfile=intent?.getStringExtra("panelprofile")!!
        //strPanelDescription=intent?.getStringExtra("paneldescription")!!
        postPanelName = binding.postPanelName
        postPanelProfile = binding.postPanelProfile
        //postPanelDescription = binding.postPanelDescription
        postDateUploaded = binding.postUploaded
        postHowManySolved = binding.postHowManySolved
        postQuestion = binding.postQuestion
        postAnswer = binding.postAnswer
        giveAnswer = binding.giveAnswer
        btnSubmitAnswer = binding.btnSubmitAnswer
        postPanelName.text = strPanelName
       // postPanelDescription.text = strPanelDescription
        postDateUploaded.text = strPostUploaded
        postHowManySolved.text = "Solved:"+strHowManySolved
        postQuestion.text = strQuestion
        giveAnswerEditText=giveAnswer.editText as TextInputEditText
        Picasso.with(applicationContext).load(strPanelProfile).into(postPanelProfile)
        btnSubmitAnswer.setOnClickListener {
            strGivenAnswer = giveAnswerEditText.text.toString()
            if(strGivenAnswer.isEmpty())
            {
                Toast.makeText(applicationContext,"Must not be empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else
            {
                if(strAnswer.equals(strGivenAnswer,true))
                {
                    postAnswer.text = "Answer:"+strAnswer
                    Toast.makeText(applicationContext,"Correct Answer",Toast.LENGTH_SHORT).show()
                    val postRef = db.child("All Posts").child("QnA").child(strPostId).child("howManySolved")
                    postRef.runTransaction(object : Transaction.Handler {
                        override fun doTransaction(mutableData: MutableData): Transaction.Result {
                            var p = mutableData.getValue(Int::class.java)
                                ?: return Transaction.success(mutableData)
                             p = p + 1
                            mutableData.value = p
                            return Transaction.success(mutableData)
                        }

                        override fun onComplete(databaseError: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                            // Transaction completed
                           return
                        }
                    })
                    val solvedRef = db.child("Users").child(CurrentUserDetails.userId).child("solved").child(strUploaderId).child(strpanelId)
                    solvedRef.addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                solvedRef.runTransaction(object : Transaction.Handler {
                                    override fun doTransaction(mutableData: MutableData): Transaction.Result {
                                        var q = mutableData.getValue(Int::class.java)
                                            ?: return Transaction.success(mutableData)

                                        q += 1

                                        mutableData.value = q
                                        return Transaction.success(mutableData)
                                    }

                                    override fun onComplete(databaseError: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                                        // Transaction completed
                                        //Toast.makeText(applicationContext,databaseError.toString(),Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                            else{
                                solvedRef.setValue(1)
                            }
                        }

                    })

                }
                else
                {
                    Toast.makeText(applicationContext,"Wrong Answer",Toast.LENGTH_SHORT).show()
                }
            }
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

private operator fun Any?.plus(i: Int): Any? {
return i+1
}

