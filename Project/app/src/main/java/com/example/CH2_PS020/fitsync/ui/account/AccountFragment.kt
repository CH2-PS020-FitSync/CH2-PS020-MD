package com.example.CH2_PS020.fitsync.ui.account

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.FragmentAccountBinding
import com.example.CH2_PS020.fitsync.ui.landingpage.LandingPage
import com.example.CH2_PS020.fitsync.util.AgeConverter
import com.example.CH2_PS020.fitsync.util.ThemePreferences
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.example.CH2_PS020.fitsync.util.dataStoreTheme
import kotlinx.coroutines.launch
import java.util.Locale

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val accountViewModel by viewModels<AccountViewModel> {
        ViewModelFactory.getInstance(requireActivity(), true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAccountBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val pref = ThemePreferences.getInstance(requireActivity().dataStoreTheme)
        val viewModel = ViewModelProvider(
            requireActivity(),
            ThemesPrefViewModelFactory(pref)
        )[ThemesPrefViewModel::class.java]

        binding.apply {
            viewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive ->
                swTheme.post {
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        swTheme.isChecked = true
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        swTheme.isChecked = false
                    }
                }
            }

            swTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                viewModel.saveThemeSetting(isChecked)
            }

            layoutChangeAccount.setOnClickListener {
                startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
            }

            layoutAbout.setOnClickListener {
                startActivity(Intent(requireActivity(), AboutActivity::class.java))
            }

            layoutLogout.setOnClickListener {
                lifecycleScope.launch {
                    accountViewModel.getSession().observe(requireActivity()) { userModel ->
                        if (userModel != null && userModel.isLogin) {
                            val accessToken = userModel.accessToken

                            logoutDialog(accessToken)
                        }
                    }
                }
            }

            loadMe()
            layoutRateUs.setOnClickListener {
                rateUsDialog()
            }
        }
        return binding.root
    }

    private fun logoutDialog(accessToken: String) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_logout)

        val btnByeBye = dialog.findViewById<Button>(R.id.btn_byebye)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

        btnByeBye.setOnClickListener {
            accountViewModel.logout(accessToken).observe(requireActivity()) { result ->
                showLoading(true)
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            accountViewModel.deleteSessions()
                            startActivity(
                                Intent(
                                    requireActivity(),
                                    LandingPage::class.java
                                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            )
                            requireActivity().finish()
                        }

                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(requireActivity(), "Failed Logout", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
    private fun loadMe() {
        accountViewModel.getMe().observe(this@AccountFragment) { result ->
            showLoading(true)
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            requireContext(),
                            "Error Retrieving data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Result.Success -> {
                        showLoading(false)
                        val photoUrl = result.data.user?.photoUrl
                        val name = result.data.user?.name
                        val birthDate = result.data.user?.birthDate
                        val gender = result.data.user?.gender

                        binding.tvName.text = name
                        binding.tvAgeGender.text = resources.getString(
                            R.string.age_gender,
                            AgeConverter.calculateAge(birthDate, requireContext()),
                            gender?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                        )
                        Glide.with(binding.ivPhoto).load(photoUrl).skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.ivPhoto)
                    }
                }
            }
        }
    }

    private fun rateUsDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_rateus)

        val rating = dialog.findViewById<RatingBar>(R.id.rating)
        val btnOkay = dialog.findViewById<Button>(R.id.btn_SendRate)

        rating.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                Toast.makeText(requireContext(), "Thanks for rate us $rating", Toast.LENGTH_SHORT).show()
            }
        btnOkay.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        loadMe()
    }


}