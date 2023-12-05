package com.example.CH2_PS020.fitsync.ui.register

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.ActivityRegisterVerifBinding
import com.example.CH2_PS020.fitsync.ui.landingpage.LandingPage
import com.example.CH2_PS020.fitsync.ui.register.RegisterActivity.Companion.USER_ID
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterVerif : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterVerifBinding
    private val viewModel by viewModels<RegisterVerifViewModel> {
        ViewModelFactory.getInstance(applicationContext, true)
    }

    private lateinit var codeOTP: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterVerifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra(USER_ID)
        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
        binding.otpView.setOtpCompletionListener { code ->
            codeOTP = code
        }

        binding.btnResendCode.setOnClickListener {
            viewModel.refreshOtp(userId.toString()).observe(this) { result ->
                showLoading(true)
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            MaterialAlertDialogBuilder(
                                this@RegisterVerif,
                                R.style.ThemeOverlay_App_MaterialAlertDialog
                            ).apply {
                                setTitle(resources.getString(R.string.successResendCode))
                                setMessage(result.data.message)
                                setNegativeButton("Fill Again") { _, _ ->

                                }
                                create()
                                show()
                            }
                        }

                        is Result.Error -> {
                            showLoading(false)
                            MaterialAlertDialogBuilder(
                                this@RegisterVerif,
                                R.style.ThemeOverlay_App_MaterialAlertDialog
                            ).apply {
                                setTitle(resources.getString(R.string.failedResendCode))
                                setMessage(result.error)
                                setNegativeButton("Fill Again") { _, _ ->

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

        binding.btnVerifyAcc.setOnClickListener {
            viewModel.registerOtp(userId.toString(), codeOTP).observe(this) { result ->
                showLoading(true)
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            successDialog()
                        }

                        is Result.Error -> {
                            val errorMsg = result.error
                            failureDialog(errorMsg)
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }

                    }
                }
            }
        }
    }

    private fun successDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_success_register)

        val btnContinue = dialog.findViewById<Button>(R.id.btn_continueToLandingPage)

        btnContinue.setOnClickListener {
            startActivity(Intent(this@RegisterVerif, LandingPage::class.java))
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

    private fun failureDialog(errorMsg: String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_failed_register)

        val btnSendMeBack = dialog.findViewById<Button>(R.id.btn_sendMeBack)

        findViewById<TextView>(R.id.tv_errorCodeVerif).text = errorMsg

        btnSendMeBack.setOnClickListener {
            onBackPressed()
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}