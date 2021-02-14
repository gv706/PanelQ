package com.application.panelq.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.panelq.DateTime
import com.application.panelq.GetDateAndTime
import com.application.panelq.QnAInfo
import com.application.panelq.R
import java.util.*


class QuestionsAccountAdapter(var context: Context,var questionsAccountList:MutableList<QnAInfo>):
    RecyclerView.Adapter<QuestionsAccountAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var question  = itemView.findViewById<TextView>(R.id.question)
        var createdOn = itemView.findViewById<TextView>(R.id.date)
        var singleRowQnAAccount = itemView.findViewById<LinearLayout>(R.id.singleRowQnAAccount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.qna_account_single_row,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return questionsAccountList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val questionInfo = questionsAccountList[position]
        holder.question.text = questionInfo.question

        var strList = GetDateAndTime.getDateTime(questionInfo?.date!!)!!.split(" ")
        val strCurDate = DateTime.date.split(" ")
        when
        {
            strList[2] == strCurDate[0] -> holder.createdOn.text = String.format(Locale.getDefault(),strList[4]+" "+strList[5])
            strList[3] == strCurDate[1] -> holder.createdOn.text = String.format(Locale.getDefault(),strList[2])
            else  -> holder.createdOn.text = String.format(Locale.getDefault(),strList[0]+" "+strList[1])
        }
        /*holder.singleRowQnAAccount.setOnClickListener {

            val intent = Intent(context, SelectedPanelActivity::class.java)
            intent.putExtra("panelprofile",panelInfo.profile )
            intent.putExtra("panelname",panelInfo.name )
            intent.putExtra("paneldescription",panelInfo.description)
            intent.putExtra("panelid",panelInfo.id)
            context.startActivity(intent)
        }*/
    }
}