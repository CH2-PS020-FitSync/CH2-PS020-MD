package com.example.CH2_PS020.fitsync.ui.tracker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isInvisible
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
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.WeekDayViewContainer
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
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import com.shawnlin.numberpicker.NumberPicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale

class TrackerFragment : Fragment() {

    private lateinit var binding: FragmentTrackerBinding
    private val viewModel by viewModels<TrackerViewModel> {
        ViewModelFactory.getInstance(requireContext(), true)
    }

    //Calendar
    private val monthCalendar: CalendarView get() = binding.calendarView
    private val weekCalendar: WeekCalendarView get() = binding.weekCalendar
    private lateinit var headerTitle: TextView
    private val btPrev: ImageButton? = view?.findViewById(R.id.bt_prev_calendar)
    private val btNext: ImageButton? = view?.findViewById(R.id.bt_next_calendar)

    //Dialog
    private lateinit var dialogAddWeight: Dialog
    private lateinit var btDatePicker: ImageButton
    private lateinit var pickerWeight: NumberPicker
    private lateinit var btAdd: MaterialButton
    private val calendar = Calendar.getInstance()

    //Dummy Data
    private val dummyWeightData = generateRandomWeightEntries(30)


    //API
    private var user: User? = null
    private var bmis: List<BmisItem?>? = null
    private var latestBMI: LatestBMI? = null
    private var pickedWeight: Float? = null
    private var pickedDate: String? = null

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
        Log.d("POST BMI", "$height,$weight,$date")
        viewModel.postBMI(height, weight, date).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    Log.d("POST BMI", result.data.toString())
                    getData()
                    showLoading(false)
                }

                is Result.Error -> {

                }
            }
        }

    }

    private fun getData() {
        viewModel.getUser().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
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
                    showLoading(false)
                }

                is Result.Error -> {

                }
            }
        }
        viewModel.getBMIs().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    bmis = result.data.bmis
                    setupCalendar()
                    setupChart()
                    showLoading(false)
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
        headerTitle = view!!.findViewById(R.id.tv_month)
        setupCalendar()
        setupChart()
//        if (latestBMI != null && bmis != null) {
//            setupSlider(calculateBMI(latestBMI?.height!!.toFloat(), latestBMI?.weight!!.toFloat()))
//        }
    }

    private fun setupCalendar() {
        setupHeader()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()

        monthCalendar.isInvisible = true
        weekCalendar.isInvisible = false

        setupMonthCalendar(currentMonth, startMonth, endMonth, daysOfWeek)
        setupWeekCalendar(currentMonth, startMonth, endMonth, daysOfWeek)

    }

    private fun updateHeader() {
        val month = monthCalendar.findFirstVisibleMonth()?.yearMonth
        val week = weekCalendar.findFirstVisibleWeek()
        if (month != null) {
            headerTitle.text = month.toString()
        } else {
            headerTitle.text = week.toString()
        }
//        Log.d("UPDATE HEADER",headerTitle?.text.toString())
        Log.d("UPDATE HEADER", month.toString())
    }

    private fun setupHeader() {
//        binding.exOneYearText.text = month.year.toString()
//        binding.exOneMonthText.text = month.month.displayText(short = false)

        val header = view?.findViewById<LinearLayout>(R.id.container_day_title)?.children
        header?.map { it as TextView }?.forEachIndexed { index, textView ->
            val dayOfWeek = daysOfWeek()[index]
            val title =
                dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            textView.text = title.uppercase()
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
        btDatePicker = dialogAddWeight.findViewById(R.id.bt_pick_date)
        pickerWeight.setOnValueChangedListener { _, _, newVal ->
            pickedWeight = newVal.toFloat()
        }
        btDatePicker.setOnClickListener {
            showDatePicker()
        }
        btAdd.setOnClickListener {
            pickedWeight?.let { it1 -> updateData(it1, latestBMI?.height!!.toFloat(), pickedDate) }
            dialogAddWeight.dismiss()
        }
        dialogAddWeight.show()
    }

    private fun setupChart() {
        val dates = bmis?.map {
            convertDateFormat(it?.date)
        }
        val aaChartView = view?.findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .yAxisTitle("Weight(KG)")
            .xAxisLabelsEnabled(true)
            .backgroundColor(android.R.color.transparent)
            .dataLabelsEnabled(false)
            .zoomType(AAChartZoomType.XY)

        if (bmis.isNullOrEmpty()) {
            aaChartModel.series(arrayOf(AASeriesElement().name("Weight")))
        } else {
            aaChartModel.series(
                arrayOf(
                    AASeriesElement().name("Weight").data(bmis?.map {
                        it?.weight!!.toDouble()
                    }!!.toTypedArray())
                )
            )
                .categories(dates!!.toTypedArray())
        }
        aaChartView?.aa_drawChartWithChartModel(aaChartModel)
    }

    private fun setupWeekCalendar(
        currentMonth: YearMonth,
        startMonth: YearMonth,
        endMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>
    ) {
        weekCalendar.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                container.day = data
                container.textView.text = data.date.dayOfMonth.toString()
                val dates = bmis?.map {
                    convertDateFormat(it?.date)
                }
                if (dates != null) {
                    if (dates.contains(data.date.toString())) {
                        container.bar.visibility = View.VISIBLE
                    }
                }

            }

            override fun create(view: View): WeekDayViewContainer {
                return WeekDayViewContainer(view)
            }

        }
        weekCalendar.weekScrollListener = { updateHeader() }
        weekCalendar.setup(startMonth.atStartOfMonth(), endMonth.atEndOfMonth(), daysOfWeek.first())
        weekCalendar.scrollToWeek(currentMonth.atStartOfMonth())
    }

    private fun setupMonthCalendar(
        currentMonth: YearMonth,
        startMonth: YearMonth,
        endMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>
    ) {
        monthCalendar.dayBinder =
            object : MonthDayBinder<DayViewContainer> {
                // Called only when a new container is needed.
                override fun create(view: View) = DayViewContainer(view)

                // Called every time we need to reuse a container.
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.textView.text = data.date.dayOfMonth.toString()
                    container.day = data
                    val dates = bmis?.map {
                        convertDateFormat(it?.date)
                    }
                    if (dates != null) {
                        if (dates.contains(data.date.toString())) {
                            container.bar.visibility = View.VISIBLE
                        }
                    }
                }
            }
        monthCalendar.monthScrollListener = { updateHeader() }
        monthCalendar.setup(startMonth, endMonth, daysOfWeek.first())//month
        monthCalendar.scrollToMonth(currentMonth)
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = LocalDateTime.of(year, monthOfYear + 1, dayOfMonth, 0, 0)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val formattedDate = selectedDate.format(formatter)
                dialogAddWeight.findViewById<TextView>(R.id.tv_picked_date).text = formattedDate
                pickedDate = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}