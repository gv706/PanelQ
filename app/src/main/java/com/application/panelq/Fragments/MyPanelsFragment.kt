package com.application.panelq.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.panelq.CurrentUserDetails
import com.application.panelq.PanelInfo
import com.application.panelq.R
import com.application.panelq.activities.NewPanelActivity
import com.application.panelq.adapters.PanelAdapter
import com.google.firebase.database.*

class MyPanelsFragment(var con:Context) : Fragment() {

    lateinit var db: DatabaseReference
    lateinit var panelsList: MutableList<PanelInfo>
    lateinit var panelRecyclerView: RecyclerView
    lateinit var panelAdapter: PanelAdapter
    lateinit var newPanelbutton : Button
    lateinit var txtDisplayIfEmpty:TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view=inflater.inflate(R.layout.fragment_mypanels, container, false)
        newPanelbutton = view.findViewById(R.id.newPanelButton)
        newPanelbutton.setOnClickListener{
            startActivity(Intent(con, NewPanelActivity::class.java))
        }
        db = FirebaseDatabase.getInstance().getReference("PanelQ")
        panelRecyclerView=view.findViewById(R.id.panelRecyclerView)
        txtDisplayIfEmpty=view.findViewById(R.id.txtCheckPanelExist)
        panelRecyclerView.setHasFixedSize(true)
        panelRecyclerView.layoutManager=LinearLayoutManager(con,LinearLayoutManager.VERTICAL,false)
        db.child("Users").child(CurrentUserDetails.userId).child("Panels").addValueEventListener(
            object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(con,error.message,Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        panelsList = mutableListOf()
                        for (datasnapshot in snapshot.children) {
                            val panelInfo = datasnapshot.child("Panel Details").getValue(PanelInfo::class.java)
                            if (panelInfo != null) {
                                panelsList.add(panelInfo)
                            }
                        }
                        panelAdapter = PanelAdapter(con, panelsList)
                        panelRecyclerView.adapter = panelAdapter
                    }
                    else{
                        txtDisplayIfEmpty.visibility=View.VISIBLE
                    }
                }

            }
        )
        return view
    }


}