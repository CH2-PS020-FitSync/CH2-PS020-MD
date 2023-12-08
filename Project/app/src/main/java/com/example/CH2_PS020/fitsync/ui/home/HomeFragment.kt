package com.example.CH2_PS020.fitsync.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.FragmentHomeBinding
import com.example.CH2_PS020.fitsync.util.AgeConverter
import com.example.CH2_PS020.fitsync.util.BMICalculator
import com.example.CH2_PS020.fitsync.util.GreetingGenerator
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import java.util.Locale

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

        return binding.root
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}