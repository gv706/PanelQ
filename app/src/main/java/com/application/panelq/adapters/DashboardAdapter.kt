package com.application.panelq.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.application.panelq.DashboardPanelInfo
import com.application.panelq.PanelInfo
import com.application.panelq.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class DashboardAdapter(var context: Context,var panelsSolvedList:MutableList<DashboardPanelInfo>):
    RecyclerView.Adapter<DashboardAdapter.MyViewHolder>() {
    lateinit var db: DatabaseReference


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtPanelName  = itemView.findViewById<TextView>(R.id.txtPanelDashboard)
        var imgPanelProfile = itemView.findViewById<CircleImageView>(R.id.imgPanelDashboard)
        var solved = itemView.findViewById<TextView>(R.id.panelSolvedCount)
        var singleRowDashboard = itemView.findViewById<LinearLayout>(R.id.singleRowDashboard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dashboard_single_row,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return panelsSolvedList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        db = FirebaseDatabase.getInstance().getReference("PanelQ")
        val panelSolvedInfo=panelsSolvedList[position]
        holder.solved.text = "Solved:"+panelSolvedInfo.solved
        db.child("Users").child(panelSolvedInfo.userid!!).child("Panels").child(panelSolvedInfo.panelid!!).child("Panel Details").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val panelInfo = snapshot.getValue(PanelInfo::class.java)
                    val profile = panelInfo?.profile
                    val name = panelInfo?.name
                    holder.txtPanelName.text = name!!
                    Picasso.with(context).load(profile!!).into(holder.imgPanelProfile)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }
}