package com.example.CH2_PS020.fitsync.ui.tracker

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.BmisItem
import com.example.CH2_PS020.fitsync.api.response.LatestBMI
import com.example.CH2_PS020.fitsync.api.response.User
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.FragmentTrackerBinding
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.DayViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.MonthViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.chart.generateRandomWeightEntries
import com.example.CH2_PS020.fitsync.ui.tracker.slider.bmiToBias
import com.example.CH2_PS020.fitsync.ui.tracker.slider.bmiToColor
import com.example.CH2_PS020.fitsync.ui.tracker.slider.bmiToTextDescription
import com.example.CH2_PS020.fitsync.ui.tracker.slider.calculateBMI
import com.example.CH2_PS020.fitsync.ui.tracker.slider.convertDateFormat
import com.example.CH2_PS020.fitsync.ui.tracker.slider.formatDoubleToOneDecimalPlace
import com.example.CH2_PS020.fitsync.util.AgeConverter
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
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
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class TrackerFragment : Fragment() {

    private lateinit var binding: FragmentTrackerBinding
    private val viewModel by viewModels<TrackerViewModel> {
        ViewModelFactory.getInstance(requireContext(), true)
    }

    //Calendar
    private lateinit var calendarView: CalendarView

    //Dialog
    private lateinit var dialogAddWeight: Dialog
    private lateinit var pickerWeight: NumberPicker
    private lateinit var btAdd: MaterialButton

    //Dummy Data
    private val dummyWeightData = generateRandomWeightEntries(30)

    //API
    private var user: User? = null
    private var bmis: List<BmisItem?>? = null
    private var latestBMI: LatestBMI? = null
    private var pickedWeight: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTrackerBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ibAddWeight.setOnClickListener {
            setupDialog()
        }
        lifecycleScope.launch {
            getData()
            initViews()
        }
    }

    private fun updateData(height: Float, weight: Float, date: String? = null) {
        viewModel.postBMI(height, weight, date)
        getData()
    }

    private fun getData() {
        viewModel.getUser().observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }

                is Result.Success -> {
                    user = result.data.user
                    latestBMI = result.data.user?.latestBMI
                    setupUser()
                    setupSlider(
                        calculateBMI(
                            latestBMI?.height!!.toFloat(),
                            latestBMI?.weight!!.toFloat()
                        )
                    )
                }

                is Result.Error -> {

                }
            }
        }
        viewModel.getBMIs().observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }

                is Result.Success -> {
                    bmis = result.data.bmis
                    setupCalendar()
                    setupChart()
                }

                is Result.Error -> {

                }
            }
        }
    }

    private fun setupUser() {
        val bmi = formatDoubleToOneDecimalPlace(
            calculateBMI(
                latestBMI?.height!!.toFloat(),
                latestBMI?.weight!!.toFloat()
            )
        )
        val bmiText = bmiToTextDescription(bmi.toFloat())
        binding.apply {
            Glide.with(this@TrackerFragment)
                .load(if (user?.photoUrl.isNullOrEmpty()) R.drawable.no_photo else user?.photoUrl)
                .into(ivCardPhoto)
            tvCardName.text = user?.name
            tvCardAgeGender.text = context?.getString(
                R.string.age_gender,
                AgeConverter.calculateAge(user?.birthDate, requireContext()),
                user?.gender.toString().uppercase()
            ) ?: "NO DATA"

            tvBmiValue.text = context?.getString(
                R.string.bmi_description, bmiText,
                bmi
            )
            tvBmiValue.setTextColor(bmiToColor(bmi.toFloat()))
            cardBarBmi.setBackgroundColor(bmiToColor(bmi.toFloat()))
            tvCardHeight.text = context?.getString(R.string.height_format, user?.latestBMI?.height)
            tvCardWeight.text = context?.getString(R.string.weight_format, user?.latestBMI?.weight)
        }
    }

    private fun initViews() {
        if (latestBMI != null && bmis != null) {
            setupCalendar()
            setupChart()
            setupSlider(calculateBMI(latestBMI?.height!!.toFloat(), latestBMI?.weight!!.toFloat()))
        }
    }

    private fun setupSlider(bmi: Double) {
        //Horizontal Bias
        binding.cardBarBmi.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = bmiToBias(bmi.toFloat())
        }
        binding.tvBmiSlider.text = formatDoubleToOneDecimalPlace(bmi)
    }

    private fun setupDialog() {
        dialogAddWeight = Dialog(requireActivity())
        dialogAddWeight.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAddWeight.setContentView(R.layout.dialog_add_weight)
        dialogAddWeight.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        dialogAddWeight.setCancelable(true)

        pickerWeight = dialogAddWeight.findViewById(R.id.picker_weight)
        btAdd = dialogAddWeight.findViewById(R.id.bt_add_body_weight)
        pickerWeight.setOnValueChangedListener { _, _, newVal ->
            pickedWeight = newVal.toFloat()
        }
        btAdd.setOnClickListener {
            pickedWeight?.let { it1 -> updateData(it1, latestBMI?.height!!.toFloat()) }
            dialogAddWeight.dismiss()
        }
        dialogAddWeight.show()
    }

    private fun setupChart() {
        if (bmis != null) {
            val dates = bmis?.map {
                convertDateFormat(it?.createdAt)
            }
            val aaChartView = view?.findViewById<AAChartView>(R.id.aa_chart_view)
            val aaChartModel: AAChartModel = AAChartModel()
                .chartType(AAChartType.Line)
                .yAxisTitle("Weight(KG)")
                .xAxisLabelsEnabled(true)
                .backgroundColor(android.R.color.transparent)
                .dataLabelsEnabled(false)
                .series(
                    arrayOf(
                        AASeriesElement().name("Weight").data(bmis?.map {
                            it?.weight!!.toDouble()
                        }!!.toTypedArray())
                    )
                )
                .categories(dates!!.toTypedArray())
                .zoomType(AAChartZoomType.XY)
            aaChartView?.aa_drawChartWithChartModel(aaChartModel)
        }
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
                    val dates = bmis?.map {
                        convertDateFormat(it?.createdAt)
                    }
                    Log.d("DATE BMI", dates.toString())
                    Log.d("DATE CALENDAR", data.date.toString())
                    if (dates != null) {
                        Log.d("DATE", dates.contains(data.date.toString()).toString())
                        if (dates.contains(data.date.toString())) {
                            //container.backgroundColor = Color.RED FULL
                            //container.backgroundResource = R.drawable.background_calendar_day CIRCLE
                            container.bar.visibility = View.VISIBLE
                        }
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