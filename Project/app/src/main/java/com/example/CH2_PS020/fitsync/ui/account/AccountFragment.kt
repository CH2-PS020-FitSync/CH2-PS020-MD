package com.example.CH2_PS020.fitsync.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.databinding.FragmentAccountBinding
import com.example.CH2_PS020.fitsync.util.ThemePreferences
import com.example.CH2_PS020.fitsync.util.dataStoreTheme

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAccountBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val pref = ThemePreferences.getInstance(requireActivity().dataStoreTheme)
        val viewModel = ViewModelProvider(
            requireActivity(),
            ThemesPrefViewModelFactory(pref)
        )[ThemesPrefViewModel::class.java]

        viewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive ->
            binding.swTheme.post {
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.swTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.swTheme.isChecked = false
                }
            }

        }

        binding.swTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
        return binding.root
    }

}