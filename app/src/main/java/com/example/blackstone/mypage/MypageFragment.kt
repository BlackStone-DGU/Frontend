package com.example.yourapp.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.databinding.FragmentMyPageBinding
import com.example.blackstone.databinding.ViewProfileInputBinding

class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private var isEditing = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)

        setProfileData()
        setEditMode(false)

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.tvEdit.setOnClickListener {
            isEditing = !isEditing
            setEditMode(isEditing)
        }

        return binding.root
    }

    // ▶ 라벨 + 값 세팅 함수
    private fun setInputFromInclude(parentView: View, includeId: Int, label: String, value: String) {
        val includeView = parentView.findViewById<View>(includeId)
        val inputBinding = ViewProfileInputBinding.bind(includeView)
        inputBinding.tvLabel.text = label
        inputBinding.etValue.setText(value)
    }

    private fun setProfileData() {
        setInputFromInclude(binding.root, R.id.input_nickname, "닉네임", "컴공이")
        setInputFromInclude(binding.root, R.id.input_email, "이메일", "winnerfit@example.com")
        setInputFromInclude(binding.root, R.id.input_weight, "몸무게", "65")
        setInputFromInclude(binding.root, R.id.input_height, "키", "176")
        setInputFromInclude(binding.root, R.id.input_gender, "성별", "남자")
        setInputFromInclude(binding.root, R.id.input_birth, "생년월일", "20060514")
    }

    // ▶ 편집모드 토글 함수
    private fun setEditMode(enable: Boolean) {
        setEditable(binding.root, R.id.input_nickname, enable)
        setEditable(binding.root, R.id.input_weight, enable)
        setEditable(binding.root, R.id.input_height, enable)

        // 비편집 항목은 항상 false
        setEditable(binding.root, R.id.input_email, false)
        setEditable(binding.root, R.id.input_gender, false)
        setEditable(binding.root, R.id.input_birth, false)

        // UI 변경
        binding.tvEdit.text = if (enable) "저장하기" else "수정하기"
        binding.btnBack.visibility = if (enable) View.GONE else View.VISIBLE
    }

    // ▶ EditText 활성화 여부 및 알파값 변경
    private fun setEditable(parentView: View, includeId: Int, editable: Boolean) {
        val includeView = parentView.findViewById<View>(includeId)
        val inputBinding = ViewProfileInputBinding.bind(includeView)
        inputBinding.etValue.isEnabled = editable
        inputBinding.etValue.alpha = if (editable) 1f else 0.7f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}