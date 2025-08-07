package com.example.blackstone.health

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.data.Exercise

class HealthDetailFragment : Fragment() {

    private var latestCount: Int = 0

    companion object {
        private const val ARG_EXERCISE = "arg_exercise"

        fun newInstance(exercise: Exercise): HealthDetailFragment {
            val fragment = HealthDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_EXERCISE, exercise)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var exercise: Exercise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exercise = arguments?.getSerializable(ARG_EXERCISE) as Exercise
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_health_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ivMission = view.findViewById<ImageView>(R.id.ivMission)
        val tvCount = view.findViewById<TextView>(R.id.tvExerciseCount)
        val tvTitle = view.findViewById<TextView>(R.id.tvHeaderTitle)
        val tvBack = view.findViewById<TextView>(R.id.tvBack)

        val btnRecord = view.findViewById<Button>(R.id.btnRecord)
        val warningBox = view.findViewById<View>(R.id.warningBox)
        val btnConfirmWarning = view.findViewById<TextView>(R.id.btnConfirmWarning)

        ivMission.setImageResource(exercise.imageResId)

        tvBack.setOnClickListener {
            val result = Bundle().apply {
                putFloat("updatedCurrent", latestCount.toFloat()) // 최신 반복 횟수
            }
            parentFragmentManager.setFragmentResult("squatUpdated", result)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val isRunning = exercise.name.contains("러닝")

        if (isRunning) {
            btnRecord.visibility = View.GONE
            warningBox.visibility = View.GONE

            childFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerInner, RunningMapFragment())
                .commit()
        } else {
            btnRecord.setOnClickListener {
                warningBox.visibility = View.VISIBLE
                btnRecord.visibility = View.GONE
            }

            btnConfirmWarning.setOnClickListener {
                warningBox.visibility = View.GONE

                childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerInner, MotionCaptureFragment())
                    .commit()
            }
        }

        // 운동 이름 상단 바에 표시
        tvTitle.text = exercise.name

        // 기본 운동 수 표시
        updateExerciseCount(tvCount, exercise.current, exercise.goal, exercise.unit)

        // ✅ MotionCaptureFragment로부터 repetitionCount 수신
        parentFragmentManager.setFragmentResultListener("repetitionResult", viewLifecycleOwner) { _, bundle ->
            val count = bundle.getInt("repetitionCount", 0)
            latestCount = count

            // count 값을 업데이트
            val updatedCurrent = count
            val goal = exercise.goal
            val unit = exercise.unit
            updateExerciseCount(tvCount, updatedCurrent.toFloat(), goal, unit)
        }
    }

    // ✅ 운동 수 표시용 함수로 분리
    private fun updateExerciseCount(tv: TextView, current: Float, goal: Float, unit: String) {
        val currentStr = if (unit == "km") String.format("%.1f", current) else current.toInt().toString()
        val goalStr = if (unit == "km") String.format("%.1f", goal) else goal.toInt().toString()
        val fullText = "$currentStr/$goalStr$unit"

        val spannable = SpannableString(fullText)
        val unitStart = fullText.indexOf(unit)
        spannable.setSpan(
            AbsoluteSizeSpan(30, true),
            unitStart,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv.text = spannable
    }

    override fun onResume() {
        super.onResume()

        view?.findViewById<TextView>(R.id.tvExerciseCount)?.let { tv ->
            if (latestCount > 0) {
                updateExerciseCount(tv, latestCount.toFloat(), exercise.goal, exercise.unit)
            }
        }
    }
}