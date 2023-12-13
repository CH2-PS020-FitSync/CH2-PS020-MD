package com.example.CH2_PS020.fitsync.ui.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.databinding.ItemHomeRecommendedBinding
import com.example.CH2_PS020.fitsync.ui.workout.StartWorkout

class RecommendedAdapter :
    ListAdapter<ExercisesItem, RecommendedAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemHomeRecommendedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercisesItem: ExercisesItem) {
            binding.tvWorkoutName.text = exercisesItem.title

            if (exercisesItem.duration?.min != null) {
                binding.tvLevelMinutes.text = itemView.context.getString(R.string.level_minutes, exercisesItem.level,
                    exercisesItem.duration.min
                )
            } else {
                binding.tvLevelMinutes.text = itemView.context.getString(R.string.level_minutes, exercisesItem.level, (15).toString())
            }

            Log.e(TAG, "bind: ${exercisesItem.level}", )
            binding.tvTargetWorkouts.text = exercisesItem.bodyPart
            Glide.with(binding.ivRecWorkouts).asGif().load(exercisesItem.gif)
                .into(binding.ivRecWorkouts)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemHomeRecommendedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(it.context, StartWorkout::class.java)
            intentDetail.putExtra("varWorkouts", exercise)
            it.context.startActivity(intentDetail)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExercisesItem>() {
            override fun areItemsTheSame(oldItem: ExercisesItem, newItem: ExercisesItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ExercisesItem,
                newItem: ExercisesItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}