package com.example.blackstone.health

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.health.MotionCaptureFragment

class HealthDetailFragment : Fragment(R.layout.fragment_health_detail) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRecord = view.findViewById<Button>(R.id.btnRecord)
        val warningBox = view.findViewById<LinearLayout>(R.id.warningBox)
        val btnConfirmWarning = view.findViewById<TextView>(R.id.btnConfirmWarning)

        // 기록하기 버튼 클릭 → 주의사항 박스 보여주고 버튼 숨기기
        btnRecord.setOnClickListener {
            btnRecord.visibility = View.GONE
            warningBox.visibility = View.VISIBLE
        }

        // 확인 버튼 클릭 → MotionCaptureFragment로 화면 전환
        btnConfirmWarning.setOnClickListener {
            // 주의사항 박스 숨김
            warningBox.visibility = View.GONE

            // MotionCaptureFragment를 FrameLayout(fragmentContainerInner)에 삽입
            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerInner, MotionCaptureFragment())
                .commit()
        }
    }
}
