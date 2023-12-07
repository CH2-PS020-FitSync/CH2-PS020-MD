package com.example.CH2_PS020.fitsync.util

import kotlin.math.pow

object BMICalculator {

    fun calculateBMI(weight: String, height: String): Double {
        val weightDouble = weight.toDoubleOrNull()
        val heightDouble = height.toDoubleOrNull()

        if (weightDouble == null || heightDouble == null || weightDouble <= 0 || heightDouble <= 0) {
            throw IllegalArgumentException("Weight and height must be valid positive numbers")
        }

        val heightInMeters = heightDouble / 100
        return weightDouble / heightInMeters.pow(2)
    }

    fun getBmiCategory(bmi : Double) : String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 24.9 -> "Normal Weight"
            bmi < 29.9 -> "Overweight"
            else -> "Obese"
        }
    }
}