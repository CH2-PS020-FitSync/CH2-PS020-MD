package com.example.CH2_PS020.fitsync

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.example.CH2_PS020.fitsync.databinding.ActivityMainBinding
import com.example.CH2_PS020.fitsync.ui.account.AccountFragment
import com.example.CH2_PS020.fitsync.ui.home.HomeFragment
import com.example.CH2_PS020.fitsync.ui.tracker.TrackerFragment
import com.example.CH2_PS020.fitsync.ui.workout.WorkoutFragment
import com.example.CH2_PS020.fitsync.util.ThemePreferences
import com.example.CH2_PS020.fitsync.util.dataStoreTheme
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        themePreferences = ThemePreferences.getInstance(dataStoreTheme)
        val pref = themePreferences.getThemeSetting().asLiveData()
        pref.observe(this) {
            Handler(Looper.getMainLooper()).post{
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

        }

        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val workoutFragment = WorkoutFragment()
        val trackerFragment = TrackerFragment()
        val accountFragment = AccountFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(binding.frameLayout.id, homeFragment, "HomeFragment")
                setReorderingAllowed(true)
                commitNow()
            }
        }

        val shapeDrawable: MaterialShapeDrawable =
            binding.bottomNavigationView.background as MaterialShapeDrawable
        shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 70F)
            .setTopRightCorner(CornerFamily.ROUNDED, 70F)
            .build()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> setFragment(homeFragment)
                R.id.menu_workout -> setFragment(workoutFragment)
                R.id.menu_tracker -> setFragment(trackerFragment)
                R.id.menu_account -> setFragment(accountFragment)
                else -> false
            }
        }

    }

    private fun setFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.frameLayout.id, fragment, fragment.javaClass.simpleName)
            setReorderingAllowed(true)
            addToBackStack(null) // Untuk dapat kembali ke fragment sebelumnya
            commit()
        }
        return true
    }

}

