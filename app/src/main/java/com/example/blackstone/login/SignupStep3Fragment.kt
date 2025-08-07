package com.example.blackstone.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.databinding.FragmentSignupStep3Binding

class SignupStep3Fragment : Fragment() {

    private var _binding: FragmentSignupStep3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupStep3Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_male -> {
                    // 남자 선택됨
                }
                R.id.rb_female -> {
                    // 여자 선택됨
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}