package com.example.CH2_PS020.fitsync.ui.historyWorkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.api.response.WorkoutsItem
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.ActivityHistoryBinding
import com.example.CH2_PS020.fitsync.ui.login.LoginViewModel
import com.example.CH2_PS020.fitsync.ui.tracker.slider.localToUtc
import com.example.CH2_PS020.fitsync.ui.workout.ExerciseAdapter
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(applicationContext, true)
    }
    private var date: String? = null
    private var userWorkouts: List<WorkoutsItem?>? = null
    private var userExercises: MutableList<ExercisesItem?>? = mutableListOf()
    private var dateFrom: String? = null
    private var dateTo: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ibBack.setOnClickListener {
            finish()
        }
        date = intent.getStringExtra("date")
        Log.d("date", date.toString())
        binding.tvDate.text = date
        getData()
    }

    private fun setupQuery() {
        val zone = ZonedDateTime.now().offset
        val startDate = "${date}T00:00:00$zone"
        val endDate = "${date}T23:59:00$zone"
        dateFrom = localToUtc(startDate)
        dateTo = localToUtc(endDate)
    }

    private fun showRecyclerView(exercises: List<ExercisesItem?>?) {
        binding.rvHistoryWorkout.layoutManager = LinearLayoutManager(this)
        binding.rvHistoryWorkout.adapter = ExerciseAdapter(exercises.orEmpty().filterNotNull())
    }

    private fun getData() {
        setupQuery()
        getMeWorkouts()
    }

    private fun getMeExercises(exerciseID: String) {
        viewModel.getExerciseByID(exerciseID).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    val exercise = result.data.exercise
                    userExercises?.add(exercise)
                    showRecyclerView(userExercises)
                }

                is Result.Error -> {
                    showLoading(false)
                    showError(result.error)
                }
            }

        }
    }

    private fun getMeWorkouts() {
        viewModel.getWorkoutHistory(dateFrom = dateFrom, dateTo = dateTo).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    userWorkouts = result.data.workouts
                    userWorkouts?.forEach {
                        Log.d("userWorkouts", it?.date.toString())
                        getMeExercises(it?.exerciseId.toString())
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                    showError(result.error)
                }
            }

        }
    }
    private fun showError(msg: String) {
        showLoading(false)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            .show()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}