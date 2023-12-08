package com.example.CH2_PS020.fitsync.ui.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.CH2_PS020.fitsync.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }
    }
}