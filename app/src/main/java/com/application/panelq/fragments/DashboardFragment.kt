package com.application.panelq.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.panelq.CurrentUserDetails
import com.application.panelq.DashboardPanelInfo
import com.application.panelq.R
import com.application.panelq.adapters.DashboardAdapter
import com.application.panelq.network.ConnectionManager
import com.google.firebase.database.*

class DashboardFragment(var con:Context) : Fragment() {

    lateinit var db: DatabaseReference
    lateinit var panelsSolvedList: MutableList<DashboardPanelInfo>
    lateinit var dashboardRecyclerView: RecyclerView
    lateinit var dashboardAdapter: DashboardAdapter
    lateinit var conn : ConnectionManager
    lateinit var progressBar : ProgressBar
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        conn = ConnectionManager()
        val view=inflater.inflate(R.layout.fragment_dashboard, container, false)
        progressBar = view.findViewById(R.id.loadProgress)!!
        db = FirebaseDatabase.getInstance().getReference("PanelQ")
        dashboardRecyclerView=view.findViewById(R.id.dashboardRecyclerView)
        dashboardRecyclerView.setHasFixedSize(true)
        dashboardRecyclerView.layoutManager=LinearLayoutManager(con,LinearLayoutManager.VERTICAL,false)
        if(conn.isNetworkAvailable(context!!)) {
            progressBar.visibility = View.VISIBLE
            dashboardRecyclerView.visibility = View.GONE
            db.child("Users").child(CurrentUserDetails.userId).child("solved").addValueEventListener(
                object: ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(con,error.message,Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {

                            panelsSolvedList = mutableListOf()
                            for (datasnapshot in snapshot.children) {
                                for (child in datasnapshot.children)
                                {
                                    val solvedInfo = DashboardPanelInfo(datasnapshot.key,child.key,child.value.toString())
                                    if (solvedInfo != null) {
                                        panelsSolvedList.add(solvedInfo)
                                    }
                                }


                            }
                            dashboardAdapter = DashboardAdapter(con, panelsSolvedList)
                            dashboardRecyclerView.adapter = dashboardAdapter
                            progressBar.visibility = View.GONE
                            dashboardRecyclerView.visibility = View.VISIBLE
                            val layoutManager = LinearLayoutManager(activity)
                            val dividerItemDecoration = DividerItemDecoration(
                                dashboardRecyclerView.getContext(),
                                layoutManager.getOrientation()
                            )
                            dashboardRecyclerView.addItemDecoration(dividerItemDecoration)
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
        return view
    }


}