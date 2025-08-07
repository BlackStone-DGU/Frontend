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
import androidx.core.text.set
import androidx.fragment.app.Fragment
import com.example.blackstone.R
import com.example.blackstone.data.Exercise

class HealthDetailFragment : Fragment() {

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
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

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

        // 운동 이름 상단 바에 표시
        tvTitle.text = exercise.name

        // 개수 표시: "15/20개" 또는 "1.5/3.0km"
        val current = if (exercise.unit == "km") String.format("%.1f", exercise.current) else exercise.current.toInt().toString()
        val goal = if (exercise.unit == "km") String.format("%.1f", exercise.goal) else exercise.goal.toInt().toString()
        val fullText = "$current/$goal${exercise.unit}"

        // 단위만 30dp로 처리
        val spannable = SpannableString(fullText)
        val unitStart = fullText.indexOf(exercise.unit)
        spannable.setSpan(
            AbsoluteSizeSpan(30, true),
            unitStart,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvCount.text = spannable
    }
}