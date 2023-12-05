package com.example.CH2_PS020.fitsync.ui.tracker.chart

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

fun generateRandomWeightEntries(count: Int): List<WeightEntry> {
    val randomWeight = Random()
    val currentDate = Calendar.getInstance()
    val decimalFormat = DecimalFormat("#.#")

    return (1..count).map {
        currentDate.add(Calendar.DAY_OF_MONTH, 1)
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(currentDate.time)
        val randomWeightValue = 50.0 + randomWeight.nextDouble() * 10.0// Random weight between 50.0 and 60.0
        val randomWeightValueFormatted = String.format("%.1f",randomWeightValue).toDouble()
        WeightEntry(formattedDate,randomWeightValueFormatted )
    }
}