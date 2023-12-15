package com.example.CH2_PS020.fitsync.ui.historyWorkout

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.ui.workout.StartWorkout


class HistoryAdapter(private val exercises: List<HistoryModel?>?) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var exerciseName: TextView
        var targetWorkouts: TextView
        var levelMinutes: TextView
        var image: ImageView
        var dateDone: TextView

        init {
            exerciseName = view.findViewById(R.id.tv_workoutName)
            targetWorkouts = view.findViewById(R.id.tv_targetWorkouts)
            levelMinutes = view.findViewById(R.id.tv_levelMinutes)
            image = view.findViewById(R.id.iv_workouts)
            dateDone = view.findViewById(R.id.tv_dateDone)
        }

        fun bind(exercisesItem: HistoryModel) {
            exerciseName.text = exercisesItem.title
            targetWorkouts.text = exercisesItem.bodyPart
            levelMinutes.text = exercisesItem.duration?.desc.toString()
            Glide.with(itemView).asGif().load(exercisesItem.gif).into(image)
            dateDone.text = exercisesItem.dateDone

            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, StartWorkout::class.java)
                val exercise = ExercisesItem(
                    exercisesItem.duration,
                    exercisesItem.jpg,
                    null,
                    exercisesItem.level,
                    exercisesItem.level,
                    exercisesItem.id,
                    exercisesItem.title,
                    null,
                    exercisesItem.bodyPart,
                    exercisesItem.desc
                )
                Log.d("Exercise Extra",exercise.toString())
                intentDetail.putExtra("varWorkouts", exercise)
                itemView.context.startActivity(intentDetail)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (exercises != null) {
            exercises[position]?.let { holder.bind(it) }
        }
    }
}
