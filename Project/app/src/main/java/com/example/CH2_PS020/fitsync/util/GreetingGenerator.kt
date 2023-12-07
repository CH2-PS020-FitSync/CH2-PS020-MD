package com.example.CH2_PS020.fitsync.util

import android.content.Context
import android.provider.Settings.Global.getString
import com.example.CH2_PS020.fitsync.R
import java.util.Calendar

object GreetingGenerator {
    fun generateGreetings(context: Context) : String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        return when {
            hour in 5..12 -> context.getString(R.string.greetings_good_morning)
            hour in 12..17 -> context.getString(R.string.greetings_good_afternoon)
            hour in 17..21 -> context.getString(R.string.greetins_good_evening)
            else -> context.getString(R.string.greetings_good_night)
        }
    }
}