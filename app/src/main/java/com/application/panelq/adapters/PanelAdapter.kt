package com.application.panelq.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.panelq.PanelInfo
import com.application.panelq.R
import com.application.panelq.activities.SelectedPanelActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class PanelAdapter(var context: Context,var panelInfoList:MutableList<PanelInfo>):
    RecyclerView.Adapter<PanelAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtPanelName  = itemView.findViewById<TextView>(R.id.txtPanelName)
        var imgPanelProfile = itemView.findViewById<CircleImageView>(R.id.imgPanel)
        var singleRowPanel = itemView.findViewById<LinearLayout>(R.id.singleRowPanel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mypanels_single_row_layout,parent,false)
        )
    }

    override fun getItemCount(): Int {
       return panelInfoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val panelInfo=panelInfoList[position]
        holder.txtPanelName.text=panelInfo.name
        Picasso.with(context).load(panelInfo.profile).into(holder.imgPanelProfile)
        holder.singleRowPanel.setOnClickListener {

            val intent = Intent(context, SelectedPanelActivity::class.java)
            intent.putExtra("panelprofile",panelInfo.profile )
            intent.putExtra("panelname",panelInfo.name )
            intent.putExtra("paneldescription",panelInfo.description)
            intent.putExtra("panelid",panelInfo.id)
            context.startActivity(intent)
        }
    }
}