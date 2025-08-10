package com.example.blackstone.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.blackstone.databinding.FragmentSignupStep2Binding

class SignupStep2Fragment : Fragment() {
    private var _binding: FragmentSignupStep2Binding? = null
    private val binding get() = _binding!!

    private val vm: AuthViewModel by activityViewModels {
        (requireActivity() as SignupActivity).viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 타이핑 즉시 반영(선택)
        val updateAffiliation = {
            val univ = binding.etUniv.text?.toString()?.trim().orEmpty()
            val major = binding.etMajor.text?.toString()?.trim().orEmpty()
            vm.setAffiliation(univ + major) // ⬅ 공백 없이
        }
        binding.etUniv.doAfterTextChanged { updateAffiliation() }
        binding.etMajor.doAfterTextChanged { updateAffiliation() }
    }

    // 버튼 눌렀을 때 액티비티가 호출해 최종 수집
    fun collectStep2(): String {
        val univ = binding.etUniv.text?.toString()?.trim().orEmpty()
        val major = binding.etMajor.text?.toString()?.trim().orEmpty()
        return univ + major // ⬅ 공백 없이
    }

    override fun onDestroyView() { _binding = null; super.onDestroyView() }
}