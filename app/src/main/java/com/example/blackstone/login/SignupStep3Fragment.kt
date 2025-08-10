package com.example.blackstone.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.blackstone.R
import com.example.blackstone.databinding.FragmentSignupStep3Binding

class SignupStep3Fragment : Fragment() {
    private var _binding: FragmentSignupStep3Binding? = null
    private val binding get() = _binding!!
    private val vm: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rgGender.setOnCheckedChangeListener { _, checkedId ->
            // 지금은 서버 전송 없음. 필요하면 vm.form에 필드 추가해서 저장.
        }
        // et_weight, et_tall, et_age도 동일
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}