package com.example.CH2_PS020.fitsync.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateConverter {
    fun convertMillisToString(format : String ,timeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeMillis
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(calendar.time)
    }
}