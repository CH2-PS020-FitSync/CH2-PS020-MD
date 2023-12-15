package com.example.CH2_PS020.fitsync.ui.tracker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.BmisItem
import com.example.CH2_PS020.fitsync.api.response.BMIResponse
import com.example.CH2_PS020.fitsync.api.response.LatestBMI
import com.example.CH2_PS020.fitsync.api.response.User
import com.example.CH2_PS020.fitsync.api.response.WorkoutsItem
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.FragmentTrackerBinding
import com.example.CH2_PS020.fitsync.ui.historyWorkout.HistoryActivity
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.DayViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.calendar.WeekDayViewContainer
import com.example.CH2_PS020.fitsync.ui.tracker.slider.bmiToBias
import com.example.CH2_PS020.fitsync.ui.tracker.slider.bmiToColor
import com.example.CH2_PS020.fitsync.ui.tracker.slider.bmiToTextDescription
import com.example.CH2_PS020.fitsync.ui.tracker.slider.calculateBMI
import com.example.CH2_PS020.fitsync.ui.tracker.slider.utcToLocal
import com.example.CH2_PS020.fitsync.ui.tracker.slider.formatDoubleToOneDecimalPlace
import com.example.CH2_PS020.fitsync.util.AgeConverter
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.YearMonth
import java.time.format.TextStyle
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
    private lateinit var cbWeekMode: CheckBox


    //Dialog
    private lateinit var dialogAddWeight: Dialog
    private lateinit var pickerWeight: TextInputEditText
    private lateinit var btAdd: MaterialButton

    //API
    private var user: User? = null
    private var bmis: List<BmisItem?>? = null
    private var latestBMI: LatestBMI? = null
    private var pickedWeight: Float? = null
    private var userWorkouts: List<WorkoutsItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentTrackerBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    private fun updateData(height: Float, weight: Float) {
        val isoDateWithZone = OffsetDateTime.now()
        Log.d("POST BMI", "$height,$weight,$isoDateWithZone")
        viewModel.postBMI(height, weight, isoDateWithZone.toString()).observe(this) { result ->
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
                    showError(result.error)
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
                    showError(result.error)
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
                    //setupCalendar()
                    setupChart()
                    showLoading(false)
                }

                is Result.Error -> {
                    showError(result.error)
                }
            }
        }
        viewModel.getWorkoutHistory().observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    userWorkouts = result.data.workouts
                    setupCalendar()
                    showLoading(false)
                }

                is Result.Error -> {
                    showError(result.error)
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
                bmi.toString()
            )
            tvBmiValue.setTextColor(bmiToColor(bmi.toFloat()))
            cardBarBmi.setBackgroundColor(bmiToColor(bmi.toFloat()))
            tvCardHeight.text = context?.getString(R.string.height_format, user?.latestBMI?.height)
            tvCardWeight.text = context?.getString(R.string.weight_format, user?.latestBMI?.weight)
        }
    }

    private fun initViews() {
        headerTitle = view!!.findViewById(R.id.tv_month)
        cbWeekMode = view!!.findViewById(R.id.cb_week_mode)
        setupCalendar()
        setupChart()
    }

    private fun toggleCalendarVisibility(isWeekMode: Boolean) {
        if (isWeekMode) {
            monthCalendar.visibility = View.GONE
            weekCalendar.visibility = View.VISIBLE
        } else {
            weekCalendar.visibility = View.GONE
            monthCalendar.visibility = View.VISIBLE
        }
    }

    private fun setupCalendar() {
        setupHeader()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        val daysOfWeek = daysOfWeek()

        cbWeekMode.setOnCheckedChangeListener { _, b ->
            toggleCalendarVisibility(b)
        }

        setupMonthCalendar(currentMonth, startMonth, endMonth, daysOfWeek)
        setupWeekCalendar(currentMonth, startMonth, endMonth, daysOfWeek)

    }

    @SuppressLint("SetTextI18n")
    private fun updateHeader() {
        val month = monthCalendar.findFirstVisibleMonth()?.yearMonth
        val week = weekCalendar.findFirstVisibleWeek() ?: return
        if (cbWeekMode.isChecked) {//Week Mode
            val firstDate = week.days.first().date
            val lastDate = week.days.last().date
            if (firstDate.yearMonth == lastDate.yearMonth) {
                headerTitle.text = "${month?.month?.name}/${month?.year}"
            } else {
                headerTitle.text =
                    "${firstDate.year},${firstDate.month.ordinal} - ${lastDate.year},${lastDate.month.ordinal}"

//                headerTitle.text =
//                    "${firstDate.month.name}/${firstDate.year} - ${lastDate.month.name}/${lastDate.year}"
            }
        } else {
            headerTitle.text = "${month?.month?.name}/${month?.year}"
        }
        Log.d("UPDATE HEADER", month.toString())
    }

    private fun setupHeader() {
        val header = view?.findViewById<LinearLayout>(R.id.container_day_title)?.children
        header?.map { it as TextView }?.forEachIndexed { index, textView ->
            val dayOfWeek = daysOfWeek()[index]
            val title =
                dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            textView.text = title.uppercase()
        }
    }

    private fun setupSlider(bmi: Double) {
        binding.cardBarBmi.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = bmiToBias(bmi.toFloat())
        }
        binding.tvBmiSlider.text = formatDoubleToOneDecimalPlace(bmi).toString()
    }

    private fun setupDialog() {
        dialogAddWeight = Dialog(requireActivity())
        dialogAddWeight.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAddWeight.setContentView(R.layout.dialog_add_weight)
        dialogAddWeight.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        dialogAddWeight.setCancelable(true)

        pickerWeight = dialogAddWeight.findViewById(R.id.et_weight)
        btAdd = dialogAddWeight.findViewById(R.id.bt_add_body_weight)
        pickerWeight.doOnTextChanged { text, _, _, _ ->
            pickedWeight = text.toString().toFloat()
        }
        btAdd.setOnClickListener {
            pickedWeight?.let { it1 -> updateData(latestBMI?.height!!.toFloat(), it1) }
            dialogAddWeight.dismiss()
        }
        dialogAddWeight.show()
    }

    private fun setupChart() {
        val dates = bmis?.map {
            utcToLocal(it?.date)
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
                        formatDoubleToOneDecimalPlace(it?.weight!!.toDouble())
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
                val dates = userWorkouts?.map {
                    utcToLocal(it?.date)
                }
                if (dates != null) {
                    if (dates.contains(data.date.toString())) {
                        container.bar.visibility = View.VISIBLE
                        //click listener here,how
                        container.view.setOnClickListener {
                            val context = container.view.context
                            val intent = Intent(context,HistoryActivity::class.java)
                            intent.putExtra("date",container.day.date.toString())
                            startActivity(intent)
                        }
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
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.textView.text = data.date.dayOfMonth.toString()
                    container.day = data
                    val dates = userWorkouts?.map {
                        utcToLocal(it?.date)
                    }
                    if (dates != null) {
                        if (dates.contains(data.date.toString())) {
                            container.bar.visibility = View.VISIBLE
                            container.view.setOnClickListener {
                                val context = container.view.context
                                val intent = Intent(context,HistoryActivity::class.java)
                                intent.putExtra("date",container.day.date.toString())
                                intent.putExtra("source","tracker")
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
        monthCalendar.monthScrollListener = { updateHeader() }
        monthCalendar.setup(startMonth, endMonth, daysOfWeek.first())//month
        monthCalendar.scrollToMonth(currentMonth)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(msg: String) {
        showLoading(false)
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
            .show()
    }
}