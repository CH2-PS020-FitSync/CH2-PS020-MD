package com.example.CH2_PS020.fitsync.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.data.model.UserModel
import com.example.CH2_PS020.fitsync.databinding.ActivityLoginBinding
import com.example.CH2_PS020.fitsync.ui.forgotpassword.ForgotPasswordActivity
import com.example.CH2_PS020.fitsync.ui.home.HomeFragment
import com.example.CH2_PS020.fitsync.ui.register.RegisterActivity
import com.example.CH2_PS020.fitsync.ui.welcome.WelcomeActivity
import com.example.CH2_PS020.fitsync.util.DateConverter
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(applicationContext, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            ibBack.setOnClickListener { onBackPressed() }
            btnForwardRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            btnLoginAcc.setOnClickListener {
                lottieLifting.pauseAnimation()
                loginPhase()
            }

            binding.btnForgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }

        }

    }

    private fun loginPhase() {
        val email = binding.edtEmailLog.text.toString()
        val password = binding.edtPassLog.text.toString()

        viewModel.login(email, password).observe(this) { result ->
            showLoading(true)
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        val accessToken = result.data.user?.accessToken
                        val refreshToken = result.data.user?.refreshToken.toString()
                        val name = result.data.user?.name.toString()
                        if (accessToken != null) {
                            lifecycleScope.launch {
                                viewModel.saveSessions(
                                    UserModel(
                                        name,
                                        email,
                                        accessToken,
                                        refreshToken
                                    )
                                )
                            }
                            dialogBodyProfile(accessToken)
                        }
                    }

                    is Result.Error -> {
                        showLoading(false)
                        MaterialAlertDialogBuilder(
                            this@LoginActivity,
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

                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }

    private fun dialogBodyProfile(authorization : String) {
        val authorization = "Bearer $authorization"
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_bodyprofile)

        val btnReset = dialog.findViewById<Button>(R.id.btn_reset)
        val btnContinue = dialog.findViewById<Button>(R.id.btn_done)
        val datePicker = dialog.findViewById<ImageButton>(R.id.ib_datePicker)
        val tvDate = dialog.findViewById<TextView>(R.id.tv_date)
        val edtWieght =
            dialog.findViewById<TextInputEditText>(R.id.edt_weightBodyProfile).text.toString()
        val edtHeight =
            dialog.findViewById<TextInputEditText>(R.id.edt_heightBodyProfile).text.toString()
        val edtGoalWeight =
            dialog.findViewById<TextInputEditText>(R.id.edt_goalWeight).text.toString()

        var selectedGender: String = ""
        var selectedExercise: String = ""

        val radioGroupGender = dialog.findViewById<RadioGroup>(R.id.radioGroupGender)
        val selectedRadioButtonId = radioGroupGender.checkedRadioButtonId

        if (selectedRadioButtonId != -1) {
            val selectedRadioButton: RadioButton = dialog.findViewById(selectedRadioButtonId)
            selectedGender = selectedRadioButton.text.toString()
        }

        val radioGroupExercise = dialog.findViewById<RadioGroup>(R.id.radioGroupExercise)
        val selectedRadioButtonIdExercise = radioGroupExercise.checkedRadioButtonId

        if (selectedRadioButtonIdExercise != -1) {
            val selectedRadioButton: RadioButton =
                dialog.findViewById(selectedRadioButtonIdExercise)
            selectedExercise = when (selectedRadioButton.text) {
                getString(R.string.beginner) -> getString(R.string.value_beginner)
                getString(R.string.intermediate) -> getString(R.string.value_intermediate)
                getString(R.string.expert) -> getString(R.string.value_expert)
                else -> ""
            }
        }

        datePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(resources.getString(R.string.birthday))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build()
            datePicker.show(supportFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener {
                tvDate.text = DateConverter.convertMillisToString(
                    resources.getString(R.string.dateFormat),
                    it
                )
            }
        }

        btnContinue.setOnClickListener {
            viewModel.getPatchedMe(
                authorization,
                selectedGender, selectedExercise,
                tvDate.text.toString(), edtGoalWeight, edtHeight, edtWieght
            ).observe(this) { result ->
                showLoading(true)
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            val name = result.data.user?.name.toString()
                            val intent = Intent(this, WelcomeActivity::class.java)
                            intent.putExtra(NAME, name)
                            startActivity(intent)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            MaterialAlertDialogBuilder(
                                this@LoginActivity,
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
            dialog.dismiss()
        }

        dialog.show()
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val NAME = "name"
    }
}