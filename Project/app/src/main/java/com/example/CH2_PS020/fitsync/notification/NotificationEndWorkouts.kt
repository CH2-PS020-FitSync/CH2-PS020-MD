package com.example.CH2_PS020.fitsync.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.ui.workout.StartWorkout
import com.example.CH2_PS020.fitsync.util.NOTIFICATION_CHANNEL_ID
import com.example.CH2_PS020.fitsync.util.WORKOUT_ID
import com.example.CH2_PS020.fitsync.util.WORKOUT_TITLE

class NotificationEndWorkouts (ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val workoutId = inputData.getInt(WORKOUT_ID, 0)
    private val workoutTitle = inputData.getString(WORKOUT_TITLE)

    override fun doWork(): Result {
        val prefManager =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify =
            prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        if (shouldNotify) {
            showNotification()
        }

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification() {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(workoutTitle)
            .setContentText(applicationContext.getString(R.string.notify_content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(workoutId, builder.build())
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(applicationContext, StartWorkout::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = R.string.notify_channel_name
            val descriptionText = R.string.notify_content
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, name.toString(), importance).apply {
                    description = descriptionText.toString()
                }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}