package com.example.CH2_PS020.fitsync.ui.landingpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.CH2_PS020.fitsync.databinding.ActivityLandingPageBinding
import com.example.CH2_PS020.fitsync.ui.login.LoginActivity
import com.example.CH2_PS020.fitsync.ui.register.RegisterActivity
import com.example.CH2_PS020.fitsync.ui.register.RegisterViewModel

class LandingPage : AppCompatActivity() {
    private lateinit var binding : ActivityLandingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@LandingPage,LoginActivity::class.java))
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@LandingPage,RegisterActivity::class.java))
            }
        }


    }
}