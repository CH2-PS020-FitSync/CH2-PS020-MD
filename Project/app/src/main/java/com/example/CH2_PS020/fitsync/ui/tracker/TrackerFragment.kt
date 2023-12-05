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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.databinding.FragmentTrackerBinding
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.DayViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.MonthViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.chart.generateRandomWeightEntries
import com.example.CH2_PS020.fitsync.ui.tracker.slider.bmiToBias
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
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
    private lateinit var dialogAddWeight: Dialog
    private lateinit var pickerWeight: NumberPicker
    private lateinit var btAdd: MaterialButton

    //Dummy Data
    private val dummyWeightData = generateRandomWeightEntries(30)

    //Slider

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
        initViews()
    }

    private fun initViews() {
        setupCalendar()
        setupChart()
        setupSlider(bmi = 18.6)
    }

    private fun setupSlider(bmi: Double) {
        //Horizontal Bias
        binding.cardBarBmi.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = bmiToBias(bmi.toFloat())
        }
        binding.tvBmiSlider.text = bmi.toString()
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

        val weights = dummyWeightData.map { it.weight }
        val dates = dummyWeightData.map { it.date }

        val aaChartView = view?.findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .yAxisTitle("Weight(KG)")
            .xAxisLabelsEnabled(true)
            .backgroundColor("#ffffff")
            .dataLabelsEnabled(false)
            .series(
                arrayOf(
                    AASeriesElement().name("Weight").data(dummyWeightData.map {
                        it.weight
                    }.toTypedArray())
                )
            )
            .categories(dates.toTypedArray())
            .zoomType(AAChartZoomType.XY)
        aaChartView?.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun setupCalendar() {
        calendarView = binding.calendarView
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View): MonthViewContainer {
                return MonthViewContainer(view)
            }

            override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                calendarView.addOnItemTouchListener(object :
                    RecyclerView.SimpleOnItemTouchListener() {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        return rv.scrollState == RecyclerView.SCROLL_STATE_DRAGGING
                    }
                })
                container.currentMonth.text =
                    data.yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
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
                    val dates = dummyWeightData.map {
                        it.date
                    }
                    if (dates.contains(data.date.toString())) {
                        //container.backgroundColor = Color.RED FULL
                        //container.backgroundResource = R.drawable.background_calendar_day CIRCLE
                        container.bar.visibility = View.VISIBLE
                    }
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