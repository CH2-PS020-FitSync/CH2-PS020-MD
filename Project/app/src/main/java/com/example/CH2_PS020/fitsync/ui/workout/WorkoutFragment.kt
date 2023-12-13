package com.example.CH2_PS020.fitsync.ui.workout

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.api.response.ExercisesResponse
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.FragmentWorkoutBinding
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.google.android.material.chip.Chip

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getExercises(limit = 30).observe(this) { result ->
            handleExerciseResult(result)
        }
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.setText(searchView.text).toString()
                var query = searchView.text.toString()
                if (query.isEmpty()) {
                    viewModel.getExercises(limit = 30).observe(this@WorkoutFragment) { result ->
                        handleExerciseResult(result)
                    }
                } else {
                    viewModel.getExercises(query, limit = 30)
                        .observe(this@WorkoutFragment) { result ->
                            handleExerciseResult(result)
                        }
                }
                searchView.hide()
                false
            }
        }
    }

    private fun handleExerciseResult(result: Result<ExercisesResponse?>) {
        when (result) {
            is Result.Loading -> {
                showLoading(true)
            }

            is Result.Success -> {
                Log.d("EXERCISES", result.data?.exercises?.size.toString())
                showRecyclerView(result.data?.exercises)
                showLoading(false)
            }

            is Result.Error -> {
                showLoading(false)
                Toast.makeText(requireContext(), "Error : ${result.error}", Toast.LENGTH_SHORT)
                    .show()
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