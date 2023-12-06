package com.example.CH2_PS020.fitsync.ui.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem

class ExerciseAdapter(private val exercises: List<ExercisesItem?>?) :
    RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var exerciseName: TextView
        var targetWorkouts: TextView
        var levelMinutes: TextView
        var image: ImageView

        init {
            exerciseName = view.findViewById(R.id.tv_workoutName)
            targetWorkouts = view.findViewById(R.id.tv_targetWorkouts)
            levelMinutes = view.findViewById(R.id.tv_levelMinutes)
            image = view.findViewById(R.id.iv_workouts)
        }

        fun bind(exercisesItem: ExercisesItem) {
            exerciseName.text = exercisesItem.title
            targetWorkouts.text = exercisesItem.bodyPart
            levelMinutes.text = exercisesItem.duration?.desc.toString()
            Glide.with(itemView).load(exercisesItem.jpg).into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_all_workouts, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return exercises?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (exercises!=null){
            exercises[position]?.let { holder.bind(it) }
        }
    }
}