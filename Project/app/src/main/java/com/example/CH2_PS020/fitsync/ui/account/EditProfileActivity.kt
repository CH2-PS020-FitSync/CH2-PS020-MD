package com.example.CH2_PS020.fitsync.ui.account

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permissions request granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissions request not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ibEditPhoto.setOnClickListener {
            startGallery()
        }
        binding.ivPhotoProfile

        binding.ibBack.setOnClickListener {
            onBackPressed()
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

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPhotoProfile.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}