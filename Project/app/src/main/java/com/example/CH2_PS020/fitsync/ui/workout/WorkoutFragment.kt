package com.example.CH2_PS020.fitsync.ui.workout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.databinding.FragmentTrackerBinding
import com.example.CH2_PS020.fitsync.databinding.FragmentWorkoutBinding
import com.example.CH2_PS020.fitsync.ui.login.LoginViewModel
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.example.CH2_PS020.fitsync.data.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WorkoutFragment : Fragment() {
    private lateinit var binding: FragmentWorkoutBinding
    private val viewModel by viewModels<WorkoutViewModel> {
        ViewModelFactory.getInstance(requireContext(), true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWorkoutBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getExercises(limit = 30).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    Log.d("EXERCISES", result.data.exercises?.size.toString())
                    showRecyclerView(result.data.exercises)
                    showLoading(false)
                }

                is Result.Error -> {
                    showLoading(false)
                    MaterialAlertDialogBuilder(
                        requireContext(),
                        R.style.ThemeOverlay_App_MaterialAlertDialog
                    ).apply {
                        setTitle(resources.getString(R.string.title_failed))
                        setMessage(result.error)
                        setNegativeButton(getString(R.string.fill_again)) { _, _ ->
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun showRecyclerView(exercises: List<ExercisesItem?>?) {
        binding.rvAllWorkouts.layoutManager = LinearLayoutManager(context)
        binding.rvAllWorkouts.adapter = ExerciseAdapter(exercises)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_bar_menu, menu)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}