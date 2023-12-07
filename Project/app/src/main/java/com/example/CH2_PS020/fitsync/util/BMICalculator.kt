package com.example.CH2_PS020.fitsync.util

import java.util.Locale
import kotlin.math.pow

object BMICalculator {

    fun calculateBMI(weight: String, height: String): String {
        val weightDouble = parseDoubleWithCommaAsDecimalSeparator(weight)
        val heightDouble = parseDoubleWithCommaAsDecimalSeparator(height)

        if (weightDouble == null || heightDouble == null || weightDouble <= 0 || heightDouble <= 0) {
            throw IllegalArgumentException("Weight and height must be valid positive numbers")
        }

        val heightInMeters = heightDouble / 100
        val bmi = weightDouble / heightInMeters.pow(2)
        return String.format(Locale.getDefault(), "%.2f", bmi)
    }

    private fun parseDoubleWithCommaAsDecimalSeparator(value: String): Double? {
        val cleanedValue = value.replace(",", ".")
        return try {
            cleanedValue.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun getBmiCategory(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 24.9 -> "Normal Weight"
            bmi < 29.9 -> "Overweight"
            else -> "Obese"
        }
    }
}
