package com.application.panelq.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.application.panelq.CurrentUserDetails
import com.application.panelq.QnAMoreInfo
import com.application.panelq.R
import com.application.panelq.adapters.QuestionHomeAdapter
import com.application.panelq.network.ConnectionManager
import com.google.firebase.database.*


class QuestionsHomeFragment(var con:Context) : Fragment() {

    lateinit var db: DatabaseReference
    lateinit var questionsHomeList: MutableList<QnAMoreInfo>
    lateinit var postIdList: MutableList<String>
    lateinit var questionsHomeRecyclerView: RecyclerView
    lateinit var questionHomeAdapter: QuestionHomeAdapter
    lateinit var refreshQuestionsBySwipe : SwipeRefreshLayout
    lateinit var conn : ConnectionManager
    lateinit var progressBar : ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        conn = ConnectionManager()
        val view=inflater.inflate(R.layout.fragment_questions_home, container, false)
        db = FirebaseDatabase.getInstance().getReference("PanelQ")
        progressBar = view.findViewById(R.id.loadProgress)
        questionsHomeRecyclerView=view.findViewById(R.id.questionsHomeRecyclerView)
        questionsHomeRecyclerView.setHasFixedSize(true)
        questionsHomeRecyclerView.layoutManager=LinearLayoutManager(con,LinearLayoutManager.VERTICAL,false)
        refreshQuestionsBySwipe = view.findViewById(R.id.refreshQuestionsBySwipe)!!
        updateQuestions()
        refreshQuestionsBySwipe.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(con,R.color.colorWhite))
        refreshQuestionsBySwipe.setColorSchemeColors(ContextCompat.getColor(con,R.color.colorPrimary))
        refreshQuestionsBySwipe.setOnRefreshListener {
            refreshQuestionsBySwipe.isRefreshing = true
              updateQuestions()
        }
        return view
    }

    private fun updateQuestions() {

        if(conn.isNetworkAvailable(context!!))
        {
           progressBar.visibility = View.VISIBLE
           questionsHomeRecyclerView.visibility=View.GONE
            db.child("All Posts").child("QnA").addValueEventListener(
                object: ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(con,error.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            questionsHomeList = mutableListOf()
                            postIdList = mutableListOf()
                            for (datasnapshot in snapshot.children) {
                                val questionsHomeInfo = datasnapshot.getValue(QnAMoreInfo::class.java)
                                if (questionsHomeInfo!!.userId != CurrentUserDetails.userId) {
                                    questionsHomeList.add(questionsHomeInfo)
                                    postIdList.add(datasnapshot.key.toString())

                                }
                            }
                            questionHomeAdapter = QuestionHomeAdapter(con, questionsHomeList.asReversed(),postIdList.asReversed())
                            questionsHomeRecyclerView.adapter = questionHomeAdapter
                            progressBar.visibility = View.GONE
                            questionsHomeRecyclerView.visibility=View.VISIBLE
                            val layoutManager = LinearLayoutManager(activity)
                            val dividerItemDecoration = DividerItemDecoration(
                                questionsHomeRecyclerView.getContext(),
                                layoutManager.getOrientation()
                            )
                            questionsHomeRecyclerView.addItemDecoration(dividerItemDecoration)
                        }
                        else{
                            progressBar.visibility = View.GONE
                        }
                    }

                }
            )
        }
        else
        {

        }
        refreshQuestionsBySwipe.isRefreshing = false
    }


}