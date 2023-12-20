package com.example.CH2_PS020.fitsync.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.CH2_PS020.fitsync.MainActivity
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.databinding.ActivityWelcomeBinding
import com.example.CH2_PS020.fitsync.ui.login.LoginActivity.Companion.NAME

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(NAME)
        binding.tvNameWelcome.text = name
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }, 2000)
    }
}