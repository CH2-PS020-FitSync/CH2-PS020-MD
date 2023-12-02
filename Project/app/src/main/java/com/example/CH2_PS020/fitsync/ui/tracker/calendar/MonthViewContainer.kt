package com.example.CH2_PS020.fitsync.ui.tracker.calendar

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.CH2_PS020.fitsync.R
import com.kizitonwose.calendar.view.ViewContainer

class MonthViewContainer(view: View) : ViewContainer(view) {
    // Alternatively, you can add an ID to the container layout and use findViewById()
    val titlesDayContainer = view.findViewById<ViewGroup>(R.id.container_day_title)
    val currentMonth = view.findViewById<TextView>(R.id.tv_month)
    val buttonNextMonth = view.findViewById<ImageButton>(R.id.bt_next_month)
    val buttonPrevMonth = view.findViewById<ImageButton>(R.id.bt_prev_month)
}