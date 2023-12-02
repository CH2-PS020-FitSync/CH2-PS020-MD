package com.example.CH2_PS020.fitsync.ui.tracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.example.CH2_PS020.fitsync.databinding.FragmentTrackerBinding
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.DayViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.MonthViewContainer
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class TrackerFragment : Fragment() {

    private lateinit var binding: FragmentTrackerBinding
    private lateinit var calendarView: CalendarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTrackerBinding.inflate(layoutInflater)
        calendarView = binding.calendarView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun setupCalendar() {
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View): MonthViewContainer {
                return MonthViewContainer(view)
            }

            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                // Remember that the header is reused so this will be called for each month.
                // However, the first day of the week will not change so no need to bind
                // the same view every time it is reused.
                container.currentMonth.text = data.yearMonth.toString()
                container.buttonNextMonth.setOnClickListener {
                    calendarView.scrollToMonth(data.yearMonth.nextMonth)
                }
                container.buttonPrevMonth.setOnClickListener {
                    calendarView.scrollToMonth(data.yearMonth.previousMonth)
                }
                if (container.titlesDayContainer.tag == null) {
                    container.titlesDayContainer.tag = data.yearMonth
                    container.titlesDayContainer.children.map { it as TextView }
                        .forEachIndexed { index, textView ->
                            val dayOfWeek = daysOfWeek()[index]
                            val title =
                                dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            textView.text = title
                            // In the code above, we use the same `daysOfWeek` list
                            // that was created when we set up the calendar.
                            // However, we can also get the `daysOfWeek` list from the month data:
                            // val daysOfWeek = data.weekDays.first().map { it.date.dayOfWeek }
                            // Alternatively, you can get the value for this specific index:
                            // val dayOfWeek = data.weekDays.first()[index].date.dayOfWeek
                        }
                }
            }
        }

    calendarView.dayBinder =
    object : MonthDayBinder<DayViewContainer> {
        // Called only when a new container is needed.
        override fun create(view: View) = DayViewContainer(view)

        // Called every time we need to reuse a container.
        override fun bind(container: DayViewContainer, data: CalendarDay) {
            container.textView.text = data.date.dayOfMonth.toString()
            container.day = data

        }
    }

    val currentMonth = YearMonth.now()
    val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
    val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
    val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library
    val daysOfWeek = daysOfWeek() // Available in the library
    calendarView.setup(startMonth, endMonth, daysOfWeek.first())
    calendarView.scrollToMonth(currentMonth)
}
}