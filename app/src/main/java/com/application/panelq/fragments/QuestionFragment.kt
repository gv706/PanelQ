package com.application.panelq.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.panelq.CurrentUserDetails
import com.application.panelq.QnAInfo
import com.application.panelq.R
import com.application.panelq.adapters.QuestionsAccountAdapter
import com.google.firebase.database.*

class QuestionFragment(var con:Context) : Fragment() {

    lateinit var db: DatabaseReference
    lateinit var questionsAccountList: MutableList<QnAInfo>
    lateinit var questionsAccountRecyclerView: RecyclerView
    lateinit var questionsAccountAdapter: QuestionsAccountAdapter
    lateinit var strPanelId : String
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_question, container, false)
        db = FirebaseDatabase.getInstance().getReference("PanelQ")
        questionsAccountRecyclerView=view.findViewById(R.id.questionsAccountRecyclerView)
        questionsAccountRecyclerView.setHasFixedSize(true)
        questionsAccountRecyclerView.layoutManager=LinearLayoutManager(con,LinearLayoutManager.VERTICAL,false)
        strPanelId= arguments?.getString("panelid")!!
        db.child("Users").child(CurrentUserDetails.userId).child("Panels").child(strPanelId).child("Posts").child("QnA").addValueEventListener(
            object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(con,error.message,Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        questionsAccountList = mutableListOf()
                        for (datasnapshot in snapshot.children) {
                            val questionInfo= datasnapshot.getValue(QnAInfo::class.java)
                            if (questionInfo != null) {
                                questionsAccountList.add(questionInfo)
                            }
                        }
                        questionsAccountAdapter = QuestionsAccountAdapter(con, questionsAccountList.asReversed())
                        questionsAccountRecyclerView.adapter = questionsAccountAdapter
                        val layoutManager = LinearLayoutManager(activity)
                        val dividerItemDecoration = DividerItemDecoration(
                            questionsAccountRecyclerView.getContext(),
                            layoutManager.getOrientation()
                        )
                        questionsAccountRecyclerView.addItemDecoration(dividerItemDecoration)
                    }
                }

            }
        )
        return view
    }


}