package com.example.CH2_PS020.fitsync.ui.tracker.calendar

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.CH2_PS020.fitsync.databinding.CalendarDayLayoutBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer

//Create your view container which acts as a view holder for each date cell.
// The view passed in here is the inflated day view resource which you provided.
class DayViewContainer(view:View):ViewContainer(view) {
    lateinit var day:CalendarDay

    val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
    val bar = CalendarDayLayoutBinding.bind(view).dividerCalendarDay
//    var backgroundColor:Int = Color.TRANSPARENT
//        set(value) {
//            field = value
//            view.setBackgroundColor(value)
//        }
//    var backgroundResource:Int = 0
//        set(value) {
//            field = value
//            view.setBackgroundResource(value)
//        }
    init {
        view.setOnClickListener {
            Log.d("CLICKED DATE",day.date.toString())
        }
    }
}



