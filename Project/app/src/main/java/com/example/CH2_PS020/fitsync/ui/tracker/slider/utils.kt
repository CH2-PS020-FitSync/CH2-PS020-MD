package com.example.CH2_PS020.fitsync.ui.tracker.slider

import android.graphics.Color
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
fun bmiToBias(bmi: Float): Float {
    val bias = if (bmi < 18.5) {
        0.25 * (bmi - 10) / (18.5 - 10)
    } else if (bmi < 25) {
        0.25 + 0.25 * (bmi - 18.5) / (25 - 18.5)
    } else if (bmi < 30) {
        0.5 + 0.25 * (bmi - 25) / (30 - 25)
    } else if (bmi < 40){
        0.75 + 0.25 * (bmi - 30) / (40 - 30)
    }else{
        1
    }
    return bias.toFloat()
}

fun bmiToTextDescription(bmi: Float): String {
    return if (bmi < 18.5) {
        "Underweight"
    } else if (bmi < 25) {
        "Healthy Weight"
    } else if (bmi < 30) {
        "Overweight"
    } else {
        "Obesity"
    }
}

//#C00021 UNDER/OBES
//#2FB300
//"#DDBA00" OVER
fun bmiToColor(bmi: Float): Int {
    val colorUnderObes = Color.parseColor("#C00021")
    val colorHealthy = Color.parseColor("#2FB300")
    val colorOver = Color.parseColor("#DDBA00")
    return if (bmi < 18.5) {
        colorUnderObes
    } else if (bmi < 25) {
        colorHealthy
    } else if (bmi < 30) {
        colorOver
    } else {
        colorUnderObes
    }
}

fun calculateBMI(height: Float, weight: Float): Double {
    // Convert height to meters
    val heightInMeters = height / 100

    // Calculate BMI using the formula: weight / (height * height)
    return weight / (heightInMeters * heightInMeters).toDouble()
}

fun convertDateFormat(date: String?): String {
    if (date.isNullOrEmpty()) {
        return "NO DATA"
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val localDateTime = try {
        LocalDateTime.parse(date, formatter)
    } catch (e: Exception) {
        e.printStackTrace()
        return "INVALID DATE FORMAT"
    }

    return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun formatDoubleToOneDecimalPlace(value: Double): String {
    return String.format("%.1f", value)
}

fun convertToUTC(isoDateTime: String): String {
    // Parse the input ISO datetime string
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val localDateTime = LocalDateTime.parse(isoDateTime, formatter)

    // Convert to UTC
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC)

    // Format the result back to ISO datetime string
    return zonedDateTime.format(formatter)
}

fun convertToTimeZone(utcDateTime: String): String {
    // Parse the input UTC datetime string
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val zonedDateTimeUTC = ZonedDateTime.parse(utcDateTime, formatter).withZoneSameInstant(ZoneOffset.UTC)

    // Convert to the system's default time zone
    val localDateTime = zonedDateTimeUTC.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

    // Format the result back to datetime string in the local time zone
    return localDateTime.format(formatter)
}








