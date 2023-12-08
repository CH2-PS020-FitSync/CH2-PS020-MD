package com.example.CH2_PS020.fitsync.ui.account

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.data.Result
import com.example.CH2_PS020.fitsync.databinding.ActivityEditProfileBinding
import com.example.CH2_PS020.fitsync.util.ViewModelFactory
import com.example.CH2_PS020.fitsync.util.uriToFile

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance(this, true)
    }

    private var currentImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewModel.getMe().observe(this@EditProfileActivity) { result ->
                showLoading(true)
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val name = result.data.user?.name
                            val height = result.data.user?.latestBMI?.height
                            val weight = result.data.user?.latestBMI?.weight
                            val gender = result.data.user?.gender
                            val photoUrl = result.data.user?.photoUrl

                            Log.e(
                                "EditProfileActivity",
                                "Name: $name, Height: $height, Weight: $weight, Gender: $gender"
                            )

                            edtChangeName.setText(name)
                            edtHeight.setText(height)
                            edtWeight.setText(weight)
                            if (gender.equals(getString(R.string.male), true)) {
                                radiogroup.check(R.id.rb_editMale)
                            } else if (gender.equals(getString(R.string.female), true)) {
                                radiogroup.check(R.id.rb_editFemale)
                            }
                            if (photoUrl.isNullOrBlank()) {
                                ivPhotoProfile.setImageResource(R.drawable.fitsync_logo)
                            } else {
                                Glide.with(ivPhotoProfile).load(photoUrl).skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(ivPhotoProfile)
                            }


                        }

                        is Result.Error -> {
                            showLoading(true)

                        }
                    }
                }
            }

            ibEditPhoto.setOnClickListener {
                startGallery()
            }

            btnSaveChangedProfile.setOnClickListener {
                uploadImage()
                val name = edtChangeName.text.toString()
                val weight = edtWeight.text.toString()
                val height = edtHeight.text.toString()
                var gender: String? = null

                val selectedRadioButtonId = radiogroup.checkedRadioButtonId

                if (selectedRadioButtonId != -1) {
                    gender = when (selectedRadioButtonId) {
                        rbEditMale.id -> getString(R.string.male)
                        rbEditFemale.id -> getString(R.string.female)
                        else -> null
                    }
                }
                editProfilePhase(name, height, weight, gender.toString())
            }

            ibBack.setOnClickListener {
                onBackPressed()
            }

        }

    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, applicationContext)
            showLoading(true)
            viewModel.updatePhoto(imageFile).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this, "Upload Successfully", Toast.LENGTH_SHORT).show()
                        }

                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                        }

                        is Result.Loading -> {
                            showLoading(true)
                        }
                    }
                }
            }

        }
    }

    private fun editProfilePhase(name: String, height: String, weight: String, gender: String) {
        viewModel.editProfile(name, height, weight, gender).observe(this) { result ->
            {}
            showLoading(true)
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        successDialog()
                        Handler().postDelayed({
                            onBackPressed()
                        },2500)

                    }

                    is Result.Error -> {
                        showLoading(false)

                    }

                    is Result.Loading -> {
                        showLoading(true)

                    }
                }
            }
        }
    }

    private fun successDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_change_profile)
        dialog.dismiss()
        dialog.show()
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPhotoProfile.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}