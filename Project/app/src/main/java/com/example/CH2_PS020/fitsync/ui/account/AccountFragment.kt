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
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.FragmentAccountBinding
import com.example.CH2_PS020.fitsync.ui.landingpage.LandingPage
import com.example.CH2_PS020.fitsync.util.ThemePreferences
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.example.CH2_PS020.fitsync.util.dataStoreTheme
import kotlinx.coroutines.launch

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

        binding.layoutChangeAccount.setOnClickListener {
            startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
        }

        binding.layoutAbout.setOnClickListener {
            startActivity(Intent(requireActivity(),AboutActivity::class.java))
        }

        binding.layoutLogout.setOnClickListener {
            lifecycleScope.launch {
                accountViewModel.getSession().observe(requireActivity()) { userModel ->
                    if (userModel != null && userModel.isLogin) {
                        val accessToken = userModel.accessToken

                        logoutDialog(accessToken)
                    }
                }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}