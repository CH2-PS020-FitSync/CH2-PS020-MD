package com.example.CH2_PS020.fitsync.ui.workout

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.CH2_PS020.fitsync.notification.NotificationEndWorkouts
import com.example.CH2_PS020.fitsync.util.WORKOUT_ID
import com.example.CH2_PS020.fitsync.util.WORKOUT_TITLE
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class StartWorkoutViewModel : ViewModel() {
    private var timer: CountDownTimer? = null

    private val initialTime = MutableLiveData<Long>()
    val currentTime = MutableLiveData<Long>()

    val currentTimeString = currentTime.map { time ->
        DateUtils.formatElapsedTime(time / 1000)
    }

    private val _eventCountDownFinish = MutableLiveData<Boolean>()
    val eventCountDownFinish: LiveData<Boolean> = _eventCountDownFinish


    fun setInitialTime(minuteFocus: Long) {
        val initialTimeMillis = minuteFocus * 60 * 1000
        initialTime.value = initialTimeMillis
        currentTime.value = initialTimeMillis

        timer = object : CountDownTimer(initialTimeMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                resetTimer()
            }
        }
    }

    fun startTimer(workoutId: String, workoutTitle: String) {
        viewModelScope.launch {
            timer?.start()
            scheduleReminderWork(workoutId, workoutTitle)
        }
    }

    fun resetTimer() {
        viewModelScope.launch {
            timer?.cancel()
            currentTime.value = initialTime.value
            _eventCountDownFinish.value = true
            cancelReminderWork()
        }

    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun scheduleReminderWork(workoutId: String, workoutTitle: String) {
        val data = workDataOf(
            WORKOUT_ID to workoutId,
            WORKOUT_TITLE to workoutTitle
        )

        val reminderWork = OneTimeWorkRequestBuilder<NotificationEndWorkouts>()
            .setInputData(data)
            .setInitialDelay(initialTime.value ?: 0, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance().enqueue(reminderWork)
    }

    private fun cancelReminderWork() {
        WorkManager.getInstance().cancelAllWorkByTag(NotificationEndWorkouts::class.java.simpleName)
    }

}