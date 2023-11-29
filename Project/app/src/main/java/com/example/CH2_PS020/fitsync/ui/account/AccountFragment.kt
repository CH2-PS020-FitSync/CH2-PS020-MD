package com.example.CH2_PS020.fitsync.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.CH2_PS020.fitsync.R
import com.example.CH2_PS020.fitsync.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding:FragmentAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentAccountBinding.inflate(layoutInflater)
        binding.ibLogout.setOnClickListener {
            Toast.makeText(requireActivity(),"clicked",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
}