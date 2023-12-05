package com.example.CH2_PS020.fitsync.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.ActivityRegisterBinding
import com.example.CH2_PS020.fitsync.ui.login.LoginActivity
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(applicationContext, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegAcc.setOnClickListener {
            registerPhase()
        }

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnForwardLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun registerPhase() {
        binding.apply {
            var name = edtNameReg.text.toString().trim()
            var email = edtEmailReg.text.toString().trim()
            var password = edtPassReg.text.toString().trim()
            var conPass = edtConpassReg.text.toString().trim()

            if (name.isEmpty()) {
                layoutName.error = resources.getString(R.string.layout_name_error)
                return@apply
            } else {
                layoutName.error = null
            }
            if (email.isEmpty()) {
                layoutEmail.error = resources.getString(R.string.layout_email_error1)
                return@apply
            }else if (!email.contains("@")) {
                layoutEmail.error = resources.getString(R.string.layout_email_error2)
            }else {
                layoutEmail.error = null
            }
            if (password.length < 8) {
                layoutPassword.error = resources.getString(R.string.layout_password_error)
                return@apply
            }else {
                layoutPassword.error = null
            }
            if (!conPass.contentEquals(password)) {
                layoutConfirmpassword.error = resources.getString(R.string.layout_conPass_error)
                return@apply
            }else {
                layoutConfirmpassword.error = null
            }

            name = edtNameReg.text.toString()
            email = edtEmailReg.text.toString()
            password = edtPassReg.text.toString()
            conPass = edtConpassReg.text.toString()

            viewModel.register(name, email, password, conPass)
                .observe(this@RegisterActivity) { result ->
                    showLoading(true)
                    if (result != null) {
                        when (result) {
                            is Result.Success -> {
                                showLoading(false)
                                MaterialAlertDialogBuilder(this@RegisterActivity, R.style.ThemeOverlay_App_MaterialAlertDialog).apply {
                                    setTitle(resources.getString(R.string.title_success))
                                    setMessage("Success Create an Account, please verification the Email")
                                    setPositiveButton(resources.getString(R.string.title_continue)) { _, _ ->
                                        val userId = result.data.user?.id
                                        val intentVerif = Intent(this@RegisterActivity, RegisterVerif::class.java)
                                        intentVerif.putExtra(USER_ID, userId )
                                        startActivity(intentVerif)
                                    }
                                    create()
                                    show()
                                }
                            }

                            is Result.Error -> {
                                showLoading(false)
                                MaterialAlertDialogBuilder(this@RegisterActivity, R.style.ThemeOverlay_App_MaterialAlertDialog).apply {
                                    setTitle("Something Wrong !")
                                    setMessage(result.error)
                                    setNegativeButton(resources.getString(R.string.btnFailedDialogText)) { _, _ ->

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

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        val USER_ID = "user_id"
    }
}