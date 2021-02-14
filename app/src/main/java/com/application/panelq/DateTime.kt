package com.application.panelq

import java.text.SimpleDateFormat
import java.util.*

object DateTime {

    lateinit var sdf:SimpleDateFormat
    val date:String
    get() {
        sdf = SimpleDateFormat("dd/MM/yyyy yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}