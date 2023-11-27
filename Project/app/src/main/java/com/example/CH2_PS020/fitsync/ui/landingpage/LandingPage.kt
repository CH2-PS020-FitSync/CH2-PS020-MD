package com.example.CH2_PS020.fitsync.ui.landingpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.databinding.ActivityLandingPageBinding

class LandingPage : AppCompatActivity() {
    private lateinit var binding : ActivityLandingPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}