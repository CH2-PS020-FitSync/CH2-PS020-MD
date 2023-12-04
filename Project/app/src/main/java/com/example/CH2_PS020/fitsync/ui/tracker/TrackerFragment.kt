package com.example.CH2_PS020.fitsync.ui.tracker

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.databinding.FragmentTrackerBinding
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.DayViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.MonthViewContainer
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.button.MaterialButton
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.shawnlin.numberpicker.NumberPicker
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class TrackerFragment : Fragment() {

    private lateinit var binding: FragmentTrackerBinding
    //Calendar
    private lateinit var calendarView: CalendarView
    //Dialog
    private lateinit var dialogAddWeight:Dialog
    private lateinit var pickerWeight:NumberPicker
    private lateinit var btAdd:MaterialButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTrackerBinding.inflate(layoutInflater)
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
        binding.ibAddWeight.setOnClickListener {
            setupDialog()
        }
        setupCalendar()
        setupChart()
    }

    private fun setupDialog() {
        dialogAddWeight = Dialog(requireActivity())
        dialogAddWeight.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAddWeight.setContentView(R.layout.dialog_add_weight)
        dialogAddWeight.setCancelable(true)

        pickerWeight = dialogAddWeight.findViewById(R.id.picker_weight)
        btAdd = dialogAddWeight.findViewById(R.id.bt_add_body_weight)
        btAdd.setOnClickListener {
            dialogAddWeight.dismiss()
        }
        dialogAddWeight.show()
    }

    private fun setupChart() {
        val aaChartView = view?.findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .title("title")
            .subtitle("subtitle")
            .backgroundColor("#4b2b7f")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("NewYork")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                    .name("London")
                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                    .name("Berlin")
                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            )
            )
        aaChartView?.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun setupCalendar() {
        calendarView = binding.calendarView
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View): MonthViewContainer {
                return MonthViewContainer(view)
            }

            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                calendarView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        return rv.scrollState == RecyclerView.SCROLL_STATE_DRAGGING
                    }
                })
                // Remember that the header is reused so this will be called for each month.
                // However, the first day of the week will not change so no need to bind
                // the same view every time it is reused.
                container.currentMonth.text = data.yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
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
                            textView.text = title.uppercase()
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
    calendarView.setup(startMonth, endMonth, firstDayOfWeek)
    calendarView.scrollToMonth(currentMonth)
}
}