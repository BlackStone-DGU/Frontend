package com.example.blackstone.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.blackstone.databinding.FragmentSignupStep1Binding

class SignupStep1Fragment : Fragment() {
    private var _binding: FragmentSignupStep1Binding? = null
    private val binding get() = _binding!!

    private val vm: AuthViewModel by activityViewModels {
        (requireActivity() as SignupActivity).viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 타이핑할 때 즉시 반영 (선택)
        binding.etId.doAfterTextChanged { vm.setName(it.toString().trim()) }
        binding.etEmail.doAfterTextChanged { vm.setEmail(it.toString().trim()) }
        binding.etPassword.doAfterTextChanged { vm.setPassword(it.toString()) }
        binding.etPasswordCheck.doAfterTextChanged { /* 일치 체크는 UI에서 */ }
    }

    fun collectStep1(): Triple<String, String, String> {
        val name = binding.etId.text?.toString()?.trim().orEmpty()
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val pw = binding.etPassword.text?.toString().orEmpty()
        return Triple(name, email, pw)
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}