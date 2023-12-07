package com.example.CH2_PS020.fitsync.ui.tracker.slider

import android.content.Context
import android.util.Log
import android.util.TypedValue

fun bmiToBias(bmi:Float):Float{
    val bias = if (bmi < 18.5){
        0.25 * (bmi - 10) / (18.5 - 10)
    }else if(bmi<25){
        0.25 + 0.25 * (bmi - 18.5) / (25 - 18.5)
    }else if(bmi<30){
        0.5 + 0.25 * (bmi - 25) / (30 - 25)
    } else{
        0.75 + 0.25 * (bmi - 30) / (40 - 30)
    }
    return bias.toFloat()
}

fun calculateBMI(height: Float, weight: Float): Double {
    // Convert height to meters
    val heightInMeters = height / 100

    // Calculate BMI using the formula: weight / (height * height)
    return weight / (heightInMeters * heightInMeters).toDouble()
}









