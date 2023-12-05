package com.example.CH2_PS020.fitsync.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.data.model.UserModel
import com.example.CH2_PS020.fitsync.databinding.ActivityLoginBinding
import com.example.CH2_PS020.fitsync.ui.forgotpassword.ForgotPasswordActivity
import com.example.CH2_PS020.fitsync.ui.register.RegisterActivity
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
                loginPhase()
            }

            binding.btnForgotPassword.setOnClickListener {
                startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))
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
                        val refreshToken = result.data.user?.accessToken.toString()
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
                        }
                    }

                    is Result.Error -> {
                        showLoading(false)
                        MaterialAlertDialogBuilder(this@LoginActivity, R.style.ThemeOverlay_App_MaterialAlertDialog).apply {
                            setTitle(resources.getString(R.string.title_failed))
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


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}