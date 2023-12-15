package com.example.CH2_PS020.fitsync.ui.historyWorkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.api.response.WorkoutsItem
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.ActivityHistoryBinding
import com.example.CH2_PS020.fitsync.ui.tracker.slider.localToUtc
import com.example.CH2_PS020.fitsync.ui.tracker.slider.utcToLocal
import com.example.CH2_PS020.fitsync.ui.workout.ExerciseAdapter
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import java.time.ZonedDateTime

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(applicationContext, true)
    }
    private var date: String? = null
    private var userWorkouts: List<WorkoutsItem?>? = null
    private var userHistory: MutableList<HistoryModel?>? = mutableListOf()
    private var dateFrom: String? = null
    private var dateTo: String? = null
    private lateinit var source: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        source = intent.getStringExtra("source").toString()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ibBack.setOnClickListener {
            finish()
        }
        getData()
    }

    private fun setupQuery() {
        date = intent.getStringExtra("date")
        binding.tvDate.text = date
        val zone = ZonedDateTime.now().offset
        val startDate = "${date}T00:00:00$zone"
        val endDate = "${date}T23:59:00$zone"
        dateFrom = localToUtc(startDate)
        dateTo = localToUtc(endDate)
    }

    private fun showRecyclerView(exercises: MutableList<HistoryModel?>?) {
        binding.rvHistoryWorkout.layoutManager = LinearLayoutManager(this)
        binding.rvHistoryWorkout.adapter = HistoryAdapter(exercises)
    }

    private fun getData() {
        if (source == "tracker") {
            setupQuery()
        }
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
                    if (exercise != null) {
                        val history = HistoryModel(
                            id = exercise.id,
                            gif = exercise.gif,
                            level = exercise.level,
                            jpg = exercise.jpg,
                            title = exercise.title,
                            type = exercise.type,
                            bodyPart = exercise.bodyPart,
                            desc = exercise.desc,
                            dateDone = getExerciseDateDone(exercise.id.toString()),
                            duration = exercise.duration
                        )
                        userHistory?.add(history)
                        Log.d("History List", userHistory.toString())
                        showRecyclerView(userHistory)
                    }
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

    private fun getExerciseDateDone(exerciseID: String): String {
        val workoutWithExercise = userWorkouts?.find { it?.exerciseId == exerciseID }
        return utcToLocal(workoutWithExercise?.date)
    }
}