package com.example.CH2_PS020.fitsync.ui.workout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.databinding.ActivityStartWorkoutBinding
import java.util.Locale

class StartWorkout : AppCompatActivity() {
    private lateinit var binding: ActivityStartWorkoutBinding
    private lateinit var exercise: ExercisesItem
    private var currentTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartWorkoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[StartWorkoutViewModel::class.java]

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
}