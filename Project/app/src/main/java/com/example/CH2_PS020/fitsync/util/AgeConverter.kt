package com.example.CH2_PS020.fitsync.util

import android.content.Context
import com.example.CH2_PS020.fitsync.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object AgeConverter {
    fun calculateAge(birthDate: String?, context: Context): String {

        if (birthDate.isNullOrBlank()) {
            throw IllegalArgumentException("BirthDate must be not be null or blank")
        }

        try {
            val sdf = SimpleDateFormat(context.getString(R.string.dateFormat), Locale.getDefault())
            val today = Calendar.getInstance()
            val birthCalendar = Calendar.getInstance()

            birthCalendar.time = sdf.parse(birthDate)!!

            var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

            if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--
            }

            return age.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException("Invalid date format or null date")
        }
    }
}