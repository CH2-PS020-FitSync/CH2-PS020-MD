package com.example.CH2_PS020.fitsync.ui.workout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.ActivityStartWorkoutBinding
import com.example.CH2_PS020.fitsync.ui.tracker.TrackerViewModel
import com.example.CH2_PS020.fitsync.ui.tracker.slider.calculateBMI
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import java.util.Locale

class StartWorkout : AppCompatActivity() {
    private lateinit var binding: ActivityStartWorkoutBinding
    private lateinit var exercise: ExercisesItem
    private var currentTime: Long = 0
    private val viewModel by viewModels<StartWorkoutViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val viewModel = ViewModelProvider(this)[StartWorkoutViewModel::class.java]

        exercise = intent.getParcelableExtra("varWorkouts")!!

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
        exercise.let { exercisesItem ->
            binding.apply {
                tvStartWorkoutName.text = exercisesItem.title
                tvStartDescWorkout.text = exercisesItem.desc
                tvStartMinuteWorkout.text = resources.getString(
                    R.string.level_minutes,
                    exercisesItem.level?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    },
                    exercisesItem.duration?.min
                )
                tvStartTargetWorkout.text = exercisesItem.bodyPart
                Glide.with(ivStartWorkoutPic)
                    .asGif()
                    .load(exercisesItem.gif).into(ivStartWorkoutPic)

                viewModel.setInitialTime(exercisesItem.duration?.min?.let {
                    extractValidValue(it)
                } ?: 10)

                btnStartWorkout.setOnClickListener {
                    viewModel.startTimer(exercisesItem.id!!, exercisesItem.title!!)
                    updateButtonState(true)
                }
                btnEndWorkout.setOnClickListener {
                    addToHistory(exercise.id.toString())
                    viewModel.resetTimer()
                    viewModel.eventCountDownFinish.observe(this@StartWorkout) {
                        updateButtonState(false)
                    }
                }

                viewModel.currentTime.observe(this@StartWorkout) { newTime ->
                    newTime?.let {
                        currentTime = it
                        viewModel.currentTimeString.observe(this@StartWorkout) { time ->
                            tvTimer.text = time
                        }
                    }
                }

                viewModel.eventCountDownFinish.observe(this@StartWorkout) {
                    if (it) {
                        updateButtonState(false)
                    }
                }
            }
        }
    }

    private fun addToHistory(exerciseId: String, rating: Int? = null, date: String? = null) {
        viewModel.postWorkout(exerciseId, rating, date).observe(this){result->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                }

                is Result.Error -> {
                    showError(result.error)
                }
            }
        }
    }

    private fun extractValidValue(duration: String): Long {
        val regex = Regex("""(\d+)-(\d+)""")
        val matchResult = regex.find(duration)

        return if (matchResult != null) {
            val validValue = matchResult.groupValues[2]

            validValue.toLongOrNull() ?: 0
        } else {
            0
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        binding.btnStartWorkout.isEnabled = !isRunning
        binding.btnEndWorkout.isEnabled = isRunning
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(msg: String) {
        showLoading(false)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            .show()
    }
}