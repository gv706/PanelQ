package com.application.panelq.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.application.panelq.CurrentUserDetails
import com.application.panelq.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var userName: TextView
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var userGender: TextView
    private lateinit var userTwoLetterName: TextView
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //val view=inflater.inflate(R.layout.fragment_profile, container, false)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        userName = binding.name
        userEmail = binding.email
        userPhone = binding.phone
        userGender = binding.gender
        userTwoLetterName = binding.twoLetterName
        userName.text = CurrentUserDetails.userName
        userEmail.text = CurrentUserDetails.userEmail
        userPhone.text = CurrentUserDetails.userPhone
        userGender.text = CurrentUserDetails.userGender
        userTwoLetterName.text = CurrentUserDetails.userName.first().toString()
        return binding.root
    }
}