package com.application.panelq.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.panelq.*
import com.application.panelq.R
import com.application.panelq.activities.QuestionPostActivity
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class QuestionHomeAdapter(var con: Context, var questionsHomeInfoList:MutableList<QnAMoreInfo>,var postIdList:MutableList<String>):
    RecyclerView.Adapter<QuestionHomeAdapter.MyViewHolder>() {
    lateinit var db: DatabaseReference

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtPanelName  = itemView.findViewById<TextView>(R.id.txtPanelName)
        var imgPanelProfile = itemView.findViewById<CircleImageView>(R.id.imgPanelProfile)
        var howManySolved  = itemView.findViewById<TextView>(R.id.howManySolved)
        var question  = itemView.findViewById<TextView>(R.id.question)
        var createdOn = itemView.findViewById<TextView>(R.id.createdOn)

        var singleRowQnA = itemView.findViewById<LinearLayout>(R.id.singleRowQnA)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.qna_home_single_row,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return questionsHomeInfoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        db = FirebaseDatabase.getInstance().getReference("PanelQ")

        val qnaInfo=questionsHomeInfoList[position]
        val postId = postIdList[position]
        holder.howManySolved.text="Solved:"+qnaInfo.howManySolved.toString()
        holder.question.text = qnaInfo.question
        val strList = GetDateAndTime.getDateTime(qnaInfo.date!!)!!.split(" ")
        val strCurDate = DateTime.date.split(" ")
        when
        {
            strList[2] == strCurDate[0] -> holder.createdOn.text = String.format(Locale.getDefault(),strList[4]+" "+strList[5])
            strList[3] == strCurDate[1] -> holder.createdOn.text = String.format(Locale.getDefault(),strList[2])
            else  -> holder.createdOn.text = String.format(Locale.getDefault(),strList[0]+" "+strList[1])
        }
        var uploadeddate = when
        {
            strList[2] == strCurDate[0] -> String.format(Locale.getDefault(),strList[4]+" "+strList[5])
            strList[3] == strCurDate[1] -> String.format(Locale.getDefault(),strList[2])
            else  -> String.format(Locale.getDefault(),strList[0]+" "+strList[1])
        }

        db.child("Users").child(qnaInfo.userId!!).child("Panels").child(qnaInfo.panelId!!).child("Panel Details").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val panelInfo = snapshot.getValue(PanelInfo::class.java)
                    val profile = panelInfo?.profile
                    val name = panelInfo?.name
                    holder.txtPanelName.text = name!!
                    Picasso.with(con).load(profile!!).into(holder.imgPanelProfile)
                    holder.singleRowQnA.setOnClickListener {

                        val intent = Intent(con, QuestionPostActivity::class.java)
                        intent.putExtra("question",qnaInfo.question)
                        intent.putExtra("answer",qnaInfo.answer)
                        intent.putExtra("panelid",qnaInfo.panelId)
                        intent.putExtra("postid",postId)
                        intent.putExtra("uploaderid",qnaInfo.userId)
                        intent.putExtra("howmanysolved",qnaInfo.howManySolved.toString())
                        intent.putExtra("dateuploaded",uploadeddate)
                        intent.putExtra("paneldescription",panelInfo.description)
                        intent.putExtra("panelname",panelInfo.name)
                        intent.putExtra("panelprofile",panelInfo.profile)
                        con.startActivity(intent)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(con, error.message, Toast.LENGTH_SHORT).show()
            }

    })
    }
}