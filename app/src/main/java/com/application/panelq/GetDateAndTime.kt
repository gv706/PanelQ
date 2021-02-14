package com.application.panelq

import java.text.SimpleDateFormat
import java.util.*

object GetDateAndTime {
    private lateinit var sdf : SimpleDateFormat
    fun getDateTime(dateAndTime:String):String?
    {
        sdf = SimpleDateFormat("E MMM dd HH:mm:SS z yyyy", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        val date = sdf.parse(dateAndTime)
        sdf = SimpleDateFormat("MMM dd dd/MM/yyyy yyyy hh:mm a",Locale.getDefault())
        return  sdf.format(date!!)
    }
}