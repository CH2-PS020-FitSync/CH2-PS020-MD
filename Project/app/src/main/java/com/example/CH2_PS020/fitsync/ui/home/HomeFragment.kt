package com.example.CH2_PS020.fitsync.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.api.response.ExercisesItem
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.FragmentHomeBinding
import com.example.CH2_PS020.fitsync.util.AgeConverter
import com.example.CH2_PS020.fitsync.util.BMICalculator
import com.example.CH2_PS020.fitsync.util.GreetingGenerator
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext(), true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getMe()
        getRecommended()
        getEstimatedNutrition()
        binding.ibRefresh.setOnClickListener {
            refresh()
        }
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val lastLoginDate = sharedPreferences.getString(LAST_LOGIN_DATE_KEY, "")
        val currentDate = getCurrentDate()
        val dayStreak = calculateDayStreak(lastLoginDate, currentDate)
        val dayStreakText = "Day Streak: $dayStreak"
        binding.tvDayStreak.text = dayStreakText
        if (dayStreak <= 10) {
            binding.tvQuoteStreak.text = getString(R.string.quote_dayuntil10)
        } else if (dayStreak <= 21) {
            binding.tvQuoteStreak.text = getString(R.string.quote_dayuntil21)
        } else {
            binding.tvQuoteStreak.text = getString(R.string.quote_dayuntilend)
        }



        return binding.root
    }
    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(resources.getString(R.string.dateFormat))
        return currentDate.format(formatter)
    }
    private fun calculateDayStreak(lastLoginDate: String?, currentDate: String): Int {
        if (lastLoginDate.isNullOrEmpty()) {
            return 1
        }
        val lastLogin = LocalDate.parse(lastLoginDate)
        val current = LocalDate.parse(currentDate)
        val daysBetween = ChronoUnit.DAYS.between(lastLogin, current).toInt()
        return if (daysBetween > 0) daysBetween + 1 else 1
    }

    private fun refresh() {
        getRecommended()
        showRefresh(false)
    }

    private fun getRecommended() {
        viewModel.recommendedWorkout().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        showRecyclerView(result.data.exercises)
                        showRefresh(true)
                    }

                    is Result.Error -> {
                        showLoading(false)

                    }
                }
            }
        }
    }

    private fun getMe() {
        viewModel.getMe().observe(requireActivity()) { result ->
            showLoading(true)
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val name = result.data.user?.name.toString()
                        val photoUrl = result.data.user?.photoUrl.toString()
                        val birthDate = result.data.user?.birthDate.toString()
                        val gender = result.data.user?.gender.toString()
                        val height = result.data.user?.latestBMI?.height.toString()
                        val weight = result.data.user?.latestBMI?.weight.toString()
                        val bmis = BMICalculator.calculateBMI(weight, height)
                        val goalWeight = result.data.user?.goalWeight.toString()

                        bindingData(
                            name,
                            photoUrl,
                            birthDate,
                            gender,
                            height,
                            weight,
                            bmis,
                            goalWeight
                        )
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Error Loading Data", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }

    private fun getEstimatedNutrition(){
        viewModel.getEstimatedNutrition().observe(this){result->
            when(result){
                is Result.Loading->showLoading(true)
                is Result.Success->{
                    val data = result.data.nutrition
                    if (data != null) {
                        bindingDataNutrition(data.estimatedCalories,data.estimatedCarbohydrates,data.estimatedProteinMean,data.estimatedFat)
                        showLoading(false)
                    }
                }
                is Result.Error-> {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Error Loading Data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
    private fun bindingDataNutrition(
        cal: Float?,
        carbo: Float?,
        protein: Float?,
        fat: Float?
    ){
        binding.apply {
            tvCalorie.text = getString(R.string.nutrition_calorie,cal?.roundToInt())
            tvCarbo.text = getString(R.string.nutrition_gram,carbo?.roundToInt())
            tvProtein.text = getString(R.string.nutrition_gram,protein?.roundToInt())
            tvFat.text = getString(R.string.nutrition_gram,fat?.roundToInt())
        }
    }
    fun bindingData(
        name: String,
        photoUrl: String,
        birthDate: String,
        gender: String,
        height: String,
        weight: String,
        bmis: String,
        goalWeight: String
    ) {
        binding.apply {
            tvGreetings.text = GreetingGenerator.generateGreetings(requireContext())
            tvHomeName.text = name
            cardName.text = name

            if (photoUrl.isBlank()) {
                ivPhoto.setImageDrawable(getDrawable(requireContext(), R.drawable.fitsync_logo))
            } else {
                Glide.with(ivPhoto).load(photoUrl).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(ivPhoto)
            }

            tvAgeGender.text = resources.getString(
                R.string.age_gender,
                AgeConverter.calculateAge(birthDate, requireContext()),
                gender.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            )
            if (gender.equals(resources.getString(R.string.male), true)) {
                ivGender.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_male))

            } else if (gender.equals(resources.getString(R.string.female), true)) {
                ivGender.setImageDrawable(getDrawable(requireContext(), R.drawable.ic_female))
            }

            tvCardHeight.text = resources.getString(R.string.card_height, height)
            tvCardWeight.text = resources.getString(R.string.card_weight, weight)
            tvCardBMI.text = resources.getString(R.string.card_bmi, bmis)
            tvCardGoalWeight.text = resources.getString(R.string.card_goalWeight, goalWeight)

        }
    }

    private fun showRecyclerView(exercises: List<ExercisesItem?>?) {
        binding.rvRecommendedWorkouts.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecommendedAdapter()
        adapter.submitList(exercises)
        binding.rvRecommendedWorkouts.adapter = adapter

    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showRefresh(isRefresh: Boolean) {
        binding.ibRefresh.visibility = if (isRefresh) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val LAST_LOGIN_DATE_KEY = "lastLoginDate"
    }


}