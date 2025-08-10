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
    private fun setInputFromInclude(
        parentView: View,
        includeId: Int,
        label: String,
        rawValue: String
    ) {
        val includeView = parentView.findViewById<View>(includeId)
        val inputBinding = ViewProfileInputBinding.bind(includeView)

        inputBinding.tvLabel.text = label

        // 라벨에 따른 단위 선택
        val unit = when (label) {
            "키" -> "cm"
            "몸무게" -> "kg"
            else -> ""
        }

        // 값에서 숫자와 단위 분리
        val (numberValue, valueUnit) = splitNumberAndUnit(rawValue)

        // EditText에는 숫자만 설정
        inputBinding.etValue.setText(numberValue)

        // 라벨이 지정한 단위가 있으면 tv_unit 표시, 없으면 GONE
        if (unit.isNotEmpty()) {
            inputBinding.tvUnit.text = unit
            inputBinding.tvUnit.visibility = View.VISIBLE
        } else {
            // rawValue에 이미 붙어 있는 단위(예: "83kg")가 있다면 그것도 표시
            if (valueUnit.isNotEmpty()) {
                inputBinding.tvUnit.text = valueUnit
                inputBinding.tvUnit.visibility = View.VISIBLE
            } else {
                inputBinding.tvUnit.visibility = View.GONE
            }
        }
    }

    // "178cm" 같은 문자열을 숫자 부분과 단위 부분으로 분리
    private fun splitNumberAndUnit(value: String): Pair<String, String> {
        val match = Regex("^([0-9]+(?:\\.[0-9]+)?)(.*)$").matchEntire(value.trim())
        return if (match != null) {
            val numberPart = match.groupValues[1]
            val unitPart = match.groupValues[2].trim()
            numberPart to unitPart
        } else {
            value to ""
        }
    }


    private fun setProfileData() {
        setInputFromInclude(binding.root, R.id.input_nickname, "닉네임", "컴공이")
        setInputFromInclude(binding.root, R.id.input_email, "이메일", "winnerfit@example.com")
        setInputFromInclude(binding.root, R.id.input_weight, "몸무게", "83")
        setInputFromInclude(binding.root, R.id.input_height, "키", "178")
        setInputFromInclude(binding.root, R.id.input_gender, "성별", "남자")
        setInputFromInclude(binding.root, R.id.input_birth, "생년월일", "20060412")
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

        // EditText와 유닛의 입력 가능 여부
        inputBinding.etValue.isEnabled = editable

        // 컨테이너 전체도 같은 상태가 되도록
        inputBinding.inputContainer.isEnabled = editable

        // alpha 값으로 밝기 조절
        val alpha = if (editable) 1f else 0.7f
        inputBinding.inputContainer.alpha = alpha
        inputBinding.etValue.alpha = alpha
        inputBinding.tvUnit.alpha = alpha
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}